#!/bin/zsh

export PATH="/usr/local/opt/openjdk/bin:$PATH"

cd "$CI_WORKSPACE/app-ios"
./scripts/build_kmm_framework.sh Release

base64 -d $GOOGLE_INFO_PLIST > DroidKaigi2022/Shared/GoogleService-Info.plist
