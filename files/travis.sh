#!/bin/bash

# Add appropriate files for encryption

rm glass.tar.enc
cd ..
tar cvf glass.tar "files/gplay-keys.json" "files/play.keystore" "files/play.properties" "files/test.keystore"
travis encrypt-file glass.tar --add
rm glass.tar
mv glass.tar.enc files/
