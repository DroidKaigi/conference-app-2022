// swift-tools-version: 5.6
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "DroidKaigiPackage",
    defaultLocalization: "en",
    platforms: [
        .iOS(.v15),
    ],
    products: [
        .library(
            name: "AppFeature",
            targets: ["AppFeature"]),
        .library(name: "Strings", targets: ["Strings"]),
    ],
    dependencies: [
        .package(url: "https://github.com/SwiftGen/SwiftGenPlugin", from: "6.6.2"),
    ],
    targets: [
        .target(
            name: "AppFeature",
            dependencies: [
                .target(name: "Strings"),
                .target(name: "TimetableFeature"),
            ]
        ),
        .target(
            name: "Strings",
            resources: [
                .process("swiftgen.yml"),
                .process("Resources"),
            ],
            plugins: [
                .plugin(name: "SwiftGenPlugin", package: "SwiftGenPlugin"),
            ]
        ),
        .target(
            name: "TimetableFeature",
            dependencies: [
                .target(name: "appioscombined"),
            ]
        ),
        .testTarget(
            name: "AppFeatureTests",
            dependencies: ["AppFeature"]
        ),
        .binaryTarget(
            name: "appioscombined",
            path: "../app-ios-combined/build/XCFrameworks/debug/appioscombined.xcframework"
        )
    ]
)
