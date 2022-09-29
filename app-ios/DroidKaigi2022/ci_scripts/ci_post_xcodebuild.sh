#!/bin/zsh

# Upload Symbols To Firebase
cd "$CI_WORKSPACE/app-ios"
./scripts/upload-symbols \
    -gsp "DroidKaigi2022/Shared/GoogleService-Info.plist" \
    -p "$CI_PRODUCT_PLATFORM" \
    "$CI_ARCHIVE_PATH/dSYMs/$CI_PRODUCT.app.dSYM"
