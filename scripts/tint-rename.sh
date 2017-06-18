#!/usr/bin/env bash

cd ../overlays

find . -iname "*tint*" -exec rename _tint.png .png '{}' \;
