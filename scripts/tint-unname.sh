#!/usr/bin/env bash

# $1 string
# $2 suffix to verify
endsWith() {
    if [[ "$1" == *"$2" ]]; then
        return 0
    fi
    return 1
}

cd overlays

for f in $(find . -type f); do
  if endsWith "$f" "_tint.png"; then
      newF="${f::-9}.png"
      printf "$f\n"
      mv "$f" "$newF"
  fi
done
read -p "done"
