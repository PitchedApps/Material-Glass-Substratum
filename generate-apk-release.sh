#!/usr/bin/env bash

# config
RELEASE_REPO=PitchedApps/Material-Glass-Test-Builds
USER_AUTH=PitchedApps
APK_NAME=MGS-sample
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
    sed -i -e "s/\"/'/g" builds/log.txt # replace all double quotes with single quotes
    overlayErrors="$(<builds/log.txt)"

    cat builds/log.txt
    printf "Create New Error Release\n%s\n" "$overlayErrors"

    API_JSON="$(printf '{"tag_name": "v%s","target_commitish": "master","name": "v%s","body": "Automatic Error Release v%s\n%s","draft": false,"prerelease": false}' $TRAVIS_BUILD_NUMBER $TRAVIS_BUILD_NUMBER $TRAVIS_BUILD_NUMBER "$overlayErrors")"
    newRelease="$(curl --data "$("$API_JSON")" https://api.github.com/repos/$RELEASE_REPO/releases?access_token=$GITHUB_API_KEY)"
    printf "Release Data\n%s\n" "$newRelease"
    rID="$(echo "$newRelease" | jq ".id")"
    echo "Created error release $rID"
else
    # create a new directory that will contain our generated apk
    mkdir $HOME/$VERSION_KEY/
    # copy generated apk from build folder to the folder just created
    cp -R $MODULE_NAME/build/outputs/apk/$APK_NAME.apk $HOME/$VERSION_KEY/

    echo "Create New Release"
    API_JSON="$(printf '{"tag_name": "v%s","target_commitish": "master","name": "v%s","body": "Automatic Release v%s","draft": false,"prerelease": false}' $TRAVIS_BUILD_NUMBER $TRAVIS_BUILD_NUMBER $TRAVIS_BUILD_NUMBER)"
    newRelease="$(curl --data "$API_JSON" https://api.github.com/repos/$RELEASE_REPO/releases?access_token=$GITHUB_API_KEY)"
    rID="$(echo "$newRelease" | jq ".id")"

    cd $HOME
    echo "Push apk to $rID"
    curl "https://uploads.github.com/repos/${RELEASE_REPO}/releases/${rID}/assets?access_token=${GITHUB_API_KEY}&name=${APK_NAME}-v${TRAVIS_BUILD_NUMBER}.apk" --header 'Content-Type: application/zip' --upload-file $VERSION_KEY/$APK_NAME.apk -X POST
fi

echo -e "Done\n"