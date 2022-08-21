// swift-tools-version: 5.6
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "DroidKaigiPackage",
    platforms: [
        .iOS(.v15),
    ],
    products: [
        .library(
            name: "App",
            targets: ["App"]),
    ],
    dependencies: [
    ],
    targets: [
        .target(
            name: "App",
            dependencies: [
                .target(name: "appioscombined"),
            ]
        ),
        .testTarget(
            name: "AppTests",
            dependencies: ["App"]
        ),
        .binaryTarget(
            name: "appioscombined",
            path: "../app-ios-combined/build/XCFrameworks/debug/appioscombined.xcframework"
        )
    ]
)
