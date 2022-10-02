#!/bin/zsh

export PATH="/usr/local/opt/openjdk@17/bin:$PATH"

cd "$CI_WORKSPACE/app-ios"
./scripts/build_kmm_framework.sh release prod

echo $GOOGLE_INFO_PLIST | base64 -d > DroidKaigi2022/Shared/GoogleService-Info.plist
