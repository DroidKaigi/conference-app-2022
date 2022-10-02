#!/bin/bash
cd "$(dirname "$0")"
cp $1/keystore.properties .
cp $1/prod.keystore .
mkdir src/prod/
cp $1/google-services.json src/prod/google-services.json

cd ..
./gradlew app-android:assembleProdRelease -Pbuildkonfig.flavor=prod
./gradlew app-android:bundleProdRelease -Pbuildkonfig.flavor=prod
