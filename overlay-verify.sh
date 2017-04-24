#!/bin/bash

# Compile our overlays against an android framework to ensure there are no compile errors

git clone --quiet --branch=master  https://github.com/PitchedApps/Substratum-Builder-Resources.git   master > /dev/null

overlays=$PWD/substratum/src/main/assets/overlays

cd Substratum-Builder-Resources
sh build-overlays.sh "$overlays"    # run builder

if [ -s "builds/log.txt" ]; then    # error occurred
    sh ../generate-apk-release.sh   # this will release the error
fi

exit 1
