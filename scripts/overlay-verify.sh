#!/bin/bash
resource=$(readlink -f "../../Substratum-Builder-Resources/")
overlays=$(readlink -f "../substratum/src/sample/assets/overlays")
cd "$resource"
sh build-overlays.sh "$overlays"    # run builder
printf "\nDone"