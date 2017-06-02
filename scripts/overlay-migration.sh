#!/bin/bash

# Global
cd ..
rootDir="$PWD"
output="substratum/src/sample/assets/overlays/"
skipPng=false
skipXml=false

# $1 package name
# $2 themeName
# $3 relative dir
newF() {
  local flavor="type3_$2"
  if [[ "$2" == "Clear" ]]; then
      flavor="res"
  fi
  echo "$output$1/$flavor/$3"
}

# $1 input location (file, must end with _tint.png)
# $2 tint color
# $3 output location (folder)
tint() {
    local file="$(basename ${1%.*})"
    local tintOutput="$3"
    if [ "${tintOutput: -1}" != "/" ]; then
        tintOutput="$tintOutput/"
    fi
    magick "$1" \( +clone -shave 1x1 \) -gravity center -compose out -composite \( "$1" -shave 1x1 -fill "$2" -colorize 100 \) -compose over -composite "$tintOutput${file::-5}.png"
    # magick convert "$1" -fill "$2" -tint 100 "$tintOutput${file::-5}.png"
}

# $1 string
# $2 suffix to verify
endsWith() {
    if [[ "$1" == *"$2" ]]; then
        return 0
    fi
    return 1
}

# $1 original file location
# $2 package
# $3 relative file name
tintImages() {
    local curr="$PWD"
    local absoluteF=$(readlink -f "$1")
    cd "$rootDir"
    for theme in scripts/themes/*.sh; do
        local themeName="${theme:15:-3}"
        local newF="$(newF "$2" "$themeName" "$3")"
        local newD="$(dirname "$newF")"
        [ ! -d "$newD" ] && mkdir -p "$newD"
        source "$theme"
        local color=$(mainColor)
        tint "$absoluteF" "$color" "$newD"
    done
    cd "$curr"
}

# $1 original file location
# $2 package
# $3 file name
migrateCopy() {
    local orig=$(readlink -f "$1")
    local curr="$PWD"
    cd "$rootDir"
    for theme in scripts/themes/*.sh; do
        themeName="$(basename ${theme%.*})"
        local newF="$(newF "$2" "$themeName" "$3")"
        [ ! -d "$(dirname "$newF")" ] && mkdir -p "$(dirname "$newF")"
        cp -a "$orig" "$newF"
    done
    cd "$curr"
}

# $1 content
# $2 package
# $3 relative file name
migrateXml() {
    local curr="$PWD"
    cd "$rootDir"
    # minify
    local content="$1"
    content="$(echo "$content" | perl -0777 -pe 's/<!--.*?-->//smg')"
    content="$(echo "$content" | tr '[:space:]' ' ' | tr -s ' ' )"
    content="$(echo "$content" | perl -0777 -pe 's/> </>\n</smg')"
    for theme in scripts/themes/*.sh; do
        themeName="$(basename ${theme%.*})"
        local newF="$(newF "$2" "$themeName" "$3")"
        [ ! -d "$(dirname "$newF")" ] && mkdir -p "$(dirname "$newF")"
        touch "$newF"
        source "$theme"
        themeXml "$content" > "$newF"
    done
    cd "$curr"
}

# $1 package
# $2 appcompat contents
portAppcompat() {
    local curr="$PWD"
    local conf="$2"
    printf "AppCompat Conf:\t$conf\n"
    cd "$rootDir"
    cd appcompat/core
    for f in $(find . -type f); do
        migrate "$f" "$1"
    done
    if [[ "$conf" == *"tint"* ]]; then
        cd ../tint
    else
        cd ../default
    fi
    for f in $(find . -type f); do
        migrate "$f" "$1"
    done
    printf "Done AppCompat Migration\n"
    cd "$curr"
}

# $1 file location
# $2 package
migrate() {
    local f="$1"
    local package="$2"
    local relative="${f:2}"
    if endsWith "$relative" ".xml" && $skipXml ; then
        continue
    fi
    if endsWith "$relative" ".png" && $skipPng ; then
        continue
    fi
    printf "Migrating $relative\n"
    if [ "${relative}" == "appcompat.txt" ]; then
        portAppcompat "$package" "$(<"$f")"
        continue
    fi
    if endsWith "$relative" "_tint.png" ; then
        tintImages "$f" "$package" "$relative"
        continue
    fi
    if [ "${relative##*.}" != "xml" ]; then
        migrateCopy "$f" "$package" "$relative"
        continue
    fi
    local content="$(<${f})"
    migrateXml "$content" "$package" "$relative"
}

# $1 package name
migratePackage() {
    local package="$1"
    printf "####################\nMigrating $package\n####################\n"
    if [ ! -d overlays/${package}/res ]; then
        printf "Res not found\n"
        continue
    fi
    cd "overlays/$package/res"
    for f in $(find . -type f); do
        migrate "$f" "$package"
    done
    cd "$rootDir"
}

main() {
    printf "Beginning Migration to $output\n"
    for package in overlays/*/; do
        package="${package:9:-1}" # trim to package name
        migratePackage "$package"
    done
    printf "\nDone\n"
}

#skipPng=true
#skipXml=true

#main

migratePackage com.android.settings
migratePackage com.android.systemui

#cd scripts
#sh overlay-verify.sh
exit 0
