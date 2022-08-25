// swift-tools-version: 5.6
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

var package = Package(
    name: "DroidKaigiPackage",
    platforms: [
        .iOS(.v15),
    ],
    products: [
        .library(
            name: "AppFeature",
            targets: ["AppFeature"]),
    ],
    dependencies: [
    ],
    targets: [
        .target(
            name: "AppFeature",
            dependencies: [
                .target(name: "TimetableFeature")
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
        ),
        .plugin(
            name: "SwiftLintPlugin",
            capability: .buildTool(),
            dependencies: [
                .target(name: "SwiftLintBinary"),
            ]
        ),
        .binaryTarget(
            name: "SwiftLintBinary",
            url: "https://github.com/realm/SwiftLint/releases/download/0.48.0/SwiftLintBinary-macos.artifactbundle.zip",
            checksum: "9c255e797260054296f9e4e4cd7e1339a15093d75f7c4227b9568d63edddba50"
        ),
    ]
)

// Append common plugins
package.targets = package.targets.map { target -> Target in
    if target.type == .regular || target.type == .test {
        if target.plugins == nil {
            target.plugins = []
        }
        target.plugins?.append(.plugin(name: "SwiftLintPlugin"))
    }

    return target
}
