#!/bin/zsh

export PATH="/usr/local/opt/openjdk/bin:$PATH"

cd $CI_WORKSPACE
./gradlew appioscombined:assembleXCFramework
