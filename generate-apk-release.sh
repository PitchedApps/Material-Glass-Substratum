#!/usr/bin/env bash

#create a new directory that will contain our generated apk
mkdir $HOME/MGS/
# copy generated apk from build folder to the folder just created
cp -R substratum/build/outputs/apk/MGS-sample.apk $HOME/MGS/

# go to home and setup git
echo "Clone Git"
cd $HOME
git config --global user.email "pitchedapps@gmail.com"
git config --global user.name "Pitched Apps CI"
# clone the repository in the buildApk folder
git clone --quiet --branch=master  https://AllanWang:$GITHUB_API_KEY@github.com/PitchedApps/Material-Glass-Substratum.git  master > /dev/null
# create version file
echo "Create Version File"
cd master
echo "MGS v$TRAVIS_BUILD_NUMBER" > MGS.txt

echo "Push Version File"
git add -f .
git commit -m "Travis build $TRAVIS_BUILD_NUMBER pushed [skip ci]"
git push -fq origin master > /dev/null

echo "Create New Release"
cd $HOME
API_JSON=$(printf '{"tag_name": "v%s","target_commitish": "master","name": "v%s","body": "Automatic Android Release v%s","draft": false,"prerelease": false}' $TRAVIS_BUILD_NUMBER $TRAVIS_BUILD_NUMBER $TRAVIS_BUILD_NUMBER)
newRelease=$(curl --data "$API_JSON" https://github.com/PitchedApps/Material-Glass-Substratum/releases?access_token=$GITHUB_API_KEY)
rID=`echo $newRelease | jq ".id"`
echo "Push apk to $rID"
curl "https://uploads.github.com/repos/PitchedApps/Material-Glass-Substratum/releases/${rID}/assets?access_token=${GITHUB_API_KEY}&name=MGS-Test-v${TRAVIS_BUILD_NUMBER}.apk" --header 'Content-Type: application/zip' --upload-file MGS/MGS-sample.apk -X POST

echo -e "Done\n"