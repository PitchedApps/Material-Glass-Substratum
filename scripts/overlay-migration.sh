#!/bin/bash
cd ..
output="substratum/src/main/assets/overlays/"
printf "Beginning Migration to $output\n"
# if [ -d "$output" ]; then
#     rm -r "$output"
#     printf "Cleaning dir\n"
# fi
# mkdir "$output"
for f in $(find overlays -type f); do
    relative="$(echo "$f" | cut -c 10-)"
    if [ "${relative##*.}" != "xml" ]; then
        printf "Non xml file: $relative\n"
        continue
    fi
    printf "Migrating $relative\n"
    content="$(<${f})"
    #########################################
    # We will use regex to replace all of
    # our keys with the appropriate values
    # colors        $x$
    # custom colors $x$aa   where aa is the alpha
    # drawables     $$x$
    # text          t
    # accent        a
    # background    b
    # opaque        o
    # ripple        r
    # divider       rr
    # dialog        d
    # middle        m       in between text & bg
    #########################################
    content="$(sed \
    -e 's/\$\$o\$/@android:drawable\/screen_background_dark/gi' \
    -e 's/\$\$b\$/@android:drawable\/screen_background_dark_transparent/gi' \
    -e 's/\$t\$\([a-z0-9]\{2\}\)/#\1ffffff/gi' \
    -e 's/\$t\$/#fff/gi' \
    -e 's/\$tt\$/#ddffffff/gi' \
    -e 's/\$ttt\$/#88ffffff/gi' \
    -e 's/\$b\$\([a-z0-9]\{2\}\)/#\1000000/gi' \
    -e 's/\$b\$/#80000000/gi' \
    -e 's/\$bb\$/#80111111/gi' \
    -e 's/\$bbb\$/#80222222/gi' \
    -e 's/\$d\$/#20ffffff/gi' \
    -e 's/\$o\$/#000/gi' \
    -e 's/\$oo\$/#111/gi' \
    -e 's/\$a\$/#748B96/gi' \
    -e 's/\$aa\$/#62757e/gi' \
    -e 's/\$r\$/#30ffffff/gi' \
    -e 's/\$rr\$/#10ffffff/gi' \
    -e 's/\$d\$/#d0060606/gi' \
    -e 's/\$m\$/#888/gi' \
    <<< "$content")"
    newF="$output$relative"
    [ ! -d "$(dirname "$newF")" ] && mkdir -p "$(dirname "$newF")"
    touch "$newF"
    echo "$content" > "$newF"
done
printf "\nDone"
exit 0