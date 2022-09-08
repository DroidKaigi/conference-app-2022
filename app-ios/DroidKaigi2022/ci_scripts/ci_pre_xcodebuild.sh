#!/bin/zsh

export PATH="/usr/local/opt/openjdk/bin:$PATH"
export CD=True

cd $CI_WORKSPACE
./gradlew appioscombined:assembleXCFramework
