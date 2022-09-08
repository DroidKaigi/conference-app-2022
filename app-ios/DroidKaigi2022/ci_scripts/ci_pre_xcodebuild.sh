#!/bin/zsh

cd $CI_WORKSPACE
./gradlew appioscombined:assembleXCFramework
