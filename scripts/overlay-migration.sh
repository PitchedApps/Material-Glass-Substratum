#!/bin/bash

# Global
rootDir="$PWD"
outputModule="substratum"
baseTheme="Clear"
skipPng=false
skipXml=false

# $1 tag name
tag() {
  printf "####################\nMigrating %s\n####################\n" "$1"
}

# $1 flavor
# $2 package
# $3 folder
# $4 relative path
# $5 theme
newF() {
  local base="$outputModule/src/$1/assets/overlays/$2"
  if [ -z "$4" ]; then # type file
    echo "$base/$3"
  elif [[ "$3" == "res" ]]; then # resource folder
    if [[ "$5" == "$baseTheme" ]]; then # base theme
      echo "$base/res/$4"
    else
      echo "$base/type3_$5/$4"
    fi
  else
    echo "$base/$3/$4" # direct copy; ignore theming
  fi
}

# $1 file path
mkfileP() {
  [ ! -d "$(dirname "$1")" ] && mkdir -p "$(dirname "$1")"
  touch "$1";
}

# $1 relative path
# $2 output path
migrateDirect() {
  local orig=$(readlink -f "$1")
  cd $rootDir
  [ ! -d "$(dirname "$2")" ] && mkdir -p "$(dirname "$2")"
  cp -a "$orig" "$2"
}

# printf $(newF "delta" "com.android" "type2_test" "drawables/test.xml")

# $1 input location (file, must end with _tint.png)
# $2 tint color
# $3 output location (folder)
tint() {
  local file="$(basename ${1%.*})"
  local tintOutput="$3"
  if [ "${tintOutput: -1}" != "/" ]; then
    tintOutput="$tintOutput/"
  fi
  cp "$1" "$tintOutput${file::-5}.png"
  # magick "$1" \( +clone -shave 1x1 \) -gravity center -compose out -composite \( "$1" -shave 1x1 -fill "$2" -colorize 100 \) -compose over -composite "$tintOutput${file::-5}.png"
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

# $1 flavor
# $2 package
# $3 file location
migrateCopy() {
  local orig=$(readlink -f "$3")
  cd "$rootDir"
  for theme in scripts/themes/*.sh; do
    local themeName="$(basename ${theme%.*})"
    local newF="$(newF "$1" "$2" "res" "$3" "$themeName")"
    [ ! -d "$(dirname "$newF")" ] && mkdir -p "$(dirname "$newF")"
    cp "$orig" "$newF"
  done
}

# $1 content
# $2 flavor
# $3 package
# $4 file location
migrateXml() {
  cd "$rootDir"
  # minify
  local content="$1"
  local pkg="$3"
  content="$(echo "$content" | perl -0777 -pe 's/<!--.*?-->//smg')"
  content="$(echo "$content" | tr '[:space:]' ' ' | tr -s ' ' )"
  content="$(echo "$content" | perl -0777 -pe 's/> </>\n</smg')"
  # Replace all references with package private ones
  content="$(echo "$content" | perl -0777 -pe "s/\@(style|drawable|dimen|string|id|color|layout)/@*$pkg:\1/smg")"
  for theme in scripts/themes/*.sh; do
    local themeName="$(basename ${theme%.*})"
    local newF="$(newF "$2" "$pkg" "res" "$4" "$themeName")"
    [ ! -d "$(dirname "$newF")" ] && mkdir -p "$(dirname "$newF")"
    touch "$newF"
    source "$theme"
    themeXml "$content" > "$newF"
  done
}

# $1 flavor
# $2 package
# $3 appcompat contents
portAppcompat() {
  local conf="$3"
  printf "AppCompat Conf:\t$conf\n"
  cd "$rootDir"
  cd appcompat/core
  for f in $(find . -type f); do
    migrate "$1" "$2" "$f"
  done
  if [[ "$conf" == *"tint"* ]]; then
    cd ../tint
  else
    cd ../default
  fi
  for f in $(find . -type f); do
    migrate "$1" "$2" "$f"
  done
  printf "Done AppCompat Migration\n"
}

# $1 flavor
# $2 package
# $3 file location
migrate() {
  local f=${3:2}
  if endsWith "$f" ".xml" && $skipXml ; then
    continue
  fi
  if endsWith "$f" ".png" && $skipPng ; then
    continue
  fi
  printf "Migrating $f\n"
  if [ "$f" == "appcompat.txt" ]; then
    (portAppcompat "$1" "$2" "$(<"$f")")
    continue
  fi
  if [ "${f##*.}" != "xml" ]; then
    (migrateCopy "$1" "$2" "$f")
    continue
  fi
  local content="$(<${f})"
  (migrateXml "$content" "$1" "$2" "$f")
}

main() {
  printf "Beginning Migration from $rootDir to $output\n"
  cd overlays
  for flavor in */; do
    if [[ -d "$flavor" && ! -L "$flavor" ]]; then
      flavor=${flavor::-1}
      tag "$flavor"
      cd "$flavor"
      for package in */; do
        if [[ -d "$package" && ! -L "$package" ]]; then
          package=${package::-1}
          tag "$package"
          cd "$package"
          for packageContent in *; do
            if [[ "$packageContent" == "res" ]]; then
              # theme resources; apply themes and copy over
              cd res
              for f in $(find . -type f); do
                migrate "$flavor" "$package" "$f"
              done
              cd ..
            else
              # base file/folder (eg type3/type2_*); copy over
              printf "Migrating $packageContent\n"
              local to="$(newF "$flavor" "$package" "$packageContent")"
              (migrateDirect "$packageContent" "$to")
            fi
          done
          cd ..
        fi
      done
      cd ..
    fi
  done
  printf "\nDone\n"
}

#skipPng=true
# skipXml=true

main
read -p "done"

#cd scripts
#sh overlay-verify.sh
exit 0
