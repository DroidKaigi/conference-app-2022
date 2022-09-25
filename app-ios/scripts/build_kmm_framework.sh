#!/bin/bash

SCRIPT_PATH=${0}
CONFIGURATION=${1}

cd "$(dirname $SCRIPT_PATH)/../../"
./gradlew appioscombined:assembleXCFramework

cp -r "app-ios-combined/build/XCFrameworks/$CONFIGURATION/appioscombined.xcframework" "app-ios/build"
