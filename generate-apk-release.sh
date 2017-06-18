#!/usr/bin/env bash

# config
RELEASE_REPO=PitchedApps/Material-Glass-Test-Builds
USER_AUTH=PitchedApps
MODULE_NAME=substratum
VERSION_KEY=MGS
# Make version key different from module name
git config --global user.email "pitchedapps@gmail.com"
git config --global user.name "Pitched Apps CI"

SBR=$(pwd) # save Substratum-Builder-Resources dir, or whatever dir we're at now
# go to home and setup git
echo "Clone Git"
cd $HOME
# clone the repository in the buildApk folder
git clone --quiet --branch=master  https://$USER_AUTH:$GITHUB_API_KEY@github.com/$RELEASE_REPO.git  master > /dev/null
# create version file
echo "Create Version File"
cd master
echo "$VERSION_KEY v$TRAVIS_BUILD_NUMBER" > "$VERSION_KEY.txt"
echo "Push Version File"
git remote rm origin
git remote add origin https://$USER_AUTH:$GITHUB_API_KEY@github.com/$RELEASE_REPO.git
git add -f .
git commit -m "Travis build $TRAVIS_BUILD_NUMBER pushed [skip ci]"
git push -fq origin master > /dev/null

cd $SBR
if [ -s "builds/log.txt" ]; then    # error occurred

    echo "Create New Error Release"

    API_JSON="$(printf '{"tag_name": "v%s","target_commitish": "master","name": "e%s","body": "Automatic Error Release e%s","draft": false,"prerelease": false}' $TRAVIS_BUILD_NUMBER $TRAVIS_BUILD_NUMBER $TRAVIS_BUILD_NUMBER)"
    newRelease="$(curl --data "$API_JSON" https://api.github.com/repos/$RELEASE_REPO/releases?access_token=$GITHUB_API_KEY)"
    printf "Release Data\n%s\n" "$newRelease"
    rID="$(echo "$newRelease" | jq ".id")"
    echo "Created error release $rID"
    echo "Push log to $rID"
    curl "https://uploads.github.com/repos/${RELEASE_REPO}/releases/${rID}/assets?access_token=${GITHUB_API_KEY}&name=${VERSION_KEY}-log-e${TRAVIS_BUILD_NUMBER}.txt" --header 'Content-Type: text/plain' --upload-file builds/log.txt -X POST

else
    # create a new directory that will contain our generated apk
    mkdir $HOME/$VERSION_KEY/
    # copy generated apk from build folder to the folder just created
    cp -a $MODULE_NAME/build/outputs/apk $HOME/$VERSION_KEY/

    echo "Create New Release"
    API_JSON="$(printf '{"tag_name": "v%s","target_commitish": "master","name": "v%s","body": "Automatic Release v%s","draft": false,"prerelease": false}' $TRAVIS_BUILD_NUMBER $TRAVIS_BUILD_NUMBER $TRAVIS_BUILD_NUMBER)"
    newRelease="$(curl --data "$API_JSON" https://api.github.com/repos/$RELEASE_REPO/releases?access_token=$GITHUB_API_KEY)"
    rID="$(echo "$newRelease" | jq ".id")"

    cd $HOME
    echo "Push apk to $rID"
    cd $VERSION_KEY
    for apk in *.apk; do
      apkName="${apk::-4}"
      printf "Apk $apkName\n"
      curl "https://uploads.github.com/repos/${RELEASE_REPO}/releases/${rID}/assets?access_token=${GITHUB_API_KEY}&name=${apkName}-v${TRAVIS_BUILD_NUMBER}.apk" --header 'Content-Type: application/zip' --upload-file ./$apk -X POST
    done
fi

echo -e "Done\n"
