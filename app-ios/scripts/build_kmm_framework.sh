#!/bin/bash

# Usage: ./build_kmm_framework.sh BUILD(debug/release) CONFIGURATION(dev/prod)

SCRIPT_PATH=${0}
BUILD=${1}
FLAVOR=${2}

cd "$(dirname $SCRIPT_PATH)/../../"
./gradlew appioscombined:assembleXCFramework -Pbuildkonfig.flavor=$FLAVOR

mkdir -p "app-ios/build"
cp -r "app-ios-combined/build/XCFrameworks/$BUILD/appioscombined.xcframework" "app-ios/build"
