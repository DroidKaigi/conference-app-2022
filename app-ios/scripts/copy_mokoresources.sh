#!/bin/bash

echo "♻️  Copying MokoResources..."

file="../../local.properties"

if [ -n "$CI" ]; then
  # We are in CI, don't validate file
else
  if [ ! -f "$file" ]; then
    echo "$file does not exist, please setup java home location first. If you are unsure how to do that, please consult the app-ios/README.md file"
    exit 1
  fi
fi

function prop {
    grep "${1}" ${file} | cut -d'=' -f2
}

if [ "$CI" = true ] ; then
    export PATH="/usr/local/opt/openjdk/bin:$PATH"
else
    export JAVA_HOME=$(prop 'org.gradle.java.home')
    echo "JAVA_HOME has been set by the script"
    echo $JAVA_HOME
fi

"$SRCROOT/../../gradlew" -p "$SRCROOT/../../" :appioscombined:copyFrameworkResourcesToApp \
    -Pmoko.resources.PLATFORM_NAME=$PLATFORM_NAME \
    -Pmoko.resources.CONFIGURATION=$CONFIGURATION \
    -Pmoko.resources.BUILT_PRODUCTS_DIR=$BUILT_PRODUCTS_DIR \
    -Pmoko.resources.CONTENTS_FOLDER_PATH=$CONTENTS_FOLDER_PATH
