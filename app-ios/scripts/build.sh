#!/bin/bash

TYPE=${1}
SCHEME=${2}
CONFIGURATION=${3}

PROJECT=DroidKaigi2022/DroidKaigi2022.xcodeproj
PLATFORM_IOS="iOS Simulator,name=iPhone 14 Pro,OS=16.0"

echo "⚙️  Building $SCHEME..."
set -o pipefail && xcodebuild $TYPE \
-project $PROJECT \
-scheme "$SCHEME" \
-configuration $CONFIGURATION \
-destination platform="$PLATFORM_IOS"
