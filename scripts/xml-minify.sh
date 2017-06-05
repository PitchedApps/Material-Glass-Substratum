#!/usr/bin/env bash

#printf "Starting XML Minify\n"
#cd ../overlays
#for f in $(find . -type f); do
#    if [[ "$f" == *.xml ]]; then
#        printf "$f\n"
#        content="$(perl -0777 -pe 's/<!--.*-->//smg' "$f")"
#        content="$(echo "$content" | tr '[:space:]' ' ' | tr -s ' ')"
#        echo "$content" > "$f"
#    fi
#done
#printf "Done\n"
package="com.whatsapp"
content="This<!--is-->a test @style"
content="$(echo "$content" | perl -0777 -pe 's/<!--.*?-->//smg')"
content="$(echo "$content" | tr '[:space:]' ' ' | tr -s ' ')"
content="$(echo "$content" | perl -0777 -pe "s/\@style/@*$package:style/smg")"
echo "result"
echo "$content"
read -p "Press enter to continue"
