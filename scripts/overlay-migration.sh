#!/bin/bash

cd ..
rootDir="$PWD"
output="substratum/src/sample/assets/overlays/"
printf "Beginning Migration to $output\n"
# if [ -d "$output" ]; then
#     rm -r "$output"
#     printf "Cleaning dir\n"
# fi
# mkdir "$output"
for package in overlays/*/; do
    package="${package:9:-1}" # trim to package name
    printf "####################\nMigrating $package\n####################\n"
    if [ ! -d overlays/${package}/res ]; then
        printf "Res not found\n"
        continue
    fi
    cd "overlays/$package/res"
    for f in $(find . -type f); do
        relative="${f:2}"
        absolute="$PWD"
        if [ "${relative##*.}" != "xml" ]; then
            printf "Non xml file: $relative\n"
            continue
        fi
        printf "Migrating $relative\n"
        content="$(<${f})"
        cd "$rootDir"
        for theme in scripts/themes/*.sh; do
            themeName="${theme:15:-3}"
            newF="$output$package/type3_$themeName/$relative"
            [ ! -d "$(dirname "$newF")" ] && mkdir -p "$(dirname "$newF")"
            touch "$newF"
            source "$theme"
            themeXml "$content" > "$newF"
        done
        cd "$absolute"
    done
    cd "$rootDir"
done
printf "\nDone\n"
cd scripts
sh overlay-verify.sh
exit 0