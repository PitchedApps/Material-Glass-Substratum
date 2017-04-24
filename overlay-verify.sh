#!/bin/bash

# Compile our overlays against an android framework to ensure there are no compile errors

echo "Cloning Builder"
git clone --branch=master  https://github.com/PitchedApps/Substratum-Builder-Resources.git

overlays=$PWD/substratum/src/main/assets/overlays
printf "Testing overlays at %s\n" "$overlays"

cd Substratum-Builder-Resources

echo `ls -l`
sh build-overlays.sh "$overlays"    # run builder

if [ -s "builds/log.txt" ]; then    # error occurred
    sh ../generate-apk-release.sh   # this will release the error
    exit 1
fi

echo "Done verification"
