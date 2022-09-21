#!/bin/bash

echo "♻️  Copying MokoResources..."

file="../../local.properties"

function prop {
    grep "${1}" ${file} | cut -d'=' -f2
}

if [ "$CI" = true ] ; then
    export PATH="/usr/local/opt/openjdk/bin:$PATH"
else
    export JAVA_HOME=$(prop 'org.gradle.java.home')
    echo $JAVA_HOME
fi

"$SRCROOT/../../gradlew" -p "$SRCROOT/../../" :appioscombined:copyFrameworkResourcesToApp \
    -Pmoko.resources.PLATFORM_NAME=$PLATFORM_NAME \
    -Pmoko.resources.CONFIGURATION=$CONFIGURATION \
    -Pmoko.resources.BUILT_PRODUCTS_DIR=$BUILT_PRODUCTS_DIR \
    -Pmoko.resources.CONTENTS_FOLDER_PATH=$CONTENTS_FOLDER_PATH
