#!/bin/bash

# Compile our overlays against an android framework to ensure there are no compile errors

echo "Cloning Builder"
git clone --quiet --branch=master  https://github.com/PitchedApps/Substratum-Builder-Resources.git

overlays=$PWD/substratum/src/main/assets/overlays
printf "Testing overlays at %s\n" "$overlays"

cd Substratum-Builder-Resources

sh build-overlays.sh "$overlays"    # run builder

if [ ! -f "builds/log.txt" ]; then  # overlays didn't build properly
    exit 1
fi

if [ -s "builds/log.txt" ]; then    # error occurred
    sh ../generate-apk-release.sh   # this will release the error
    exit 1
fi

echo "Done verification"
