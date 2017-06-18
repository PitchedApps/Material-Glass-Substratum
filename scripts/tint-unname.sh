#!/usr/bin/env bash

# $1 string
# $2 suffix to verify
endsWith() {
    if [[ "$1" == *"$2" ]]; then
        return 0
    fi
    return 1
}

printf "Starting tint rename\n"
cd ..
if [ -d factory/tint/output ]; then # clean build dir
    rm -r factory/tint/output
fi
mkdir factory/tint/output
cd factory/tint
for f in *.png; do
    name="${f::-4}"
    printf "$name\n"
    name="${name}_tint.png"
    cp -a "$f" "output/$name"
done
printf "Done\n"
