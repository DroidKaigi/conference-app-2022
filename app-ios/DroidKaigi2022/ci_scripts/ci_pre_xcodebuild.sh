#!/bin/zsh

export PATH="/usr/local/opt/openjdk/bin:$PATH"

cd "$CI_WORKSPACE/app-ios"
./scripts/build_kmm_framework.sh Release
