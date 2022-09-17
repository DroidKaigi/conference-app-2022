// swift-tools-version: 5.6
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

var package = Package(
    name: "DroidKaigiPackage",
    defaultLocalization: "en",
    platforms: [
        .iOS(.v15),
    ],
    products: [
        .library(name: "AboutFeature", targets: ["AboutFeature"]),
        .library(name: "AppFeature", targets: ["AppFeature"]),
        .library(name: "Auth", targets: ["Auth"]),
        .library(name: "CommonComponents", targets: ["CommonComponents"]),
        .library(name: "Container", targets: ["Container"]),
        .library(name: "ContributorFeature", targets: ["ContributorFeature"]),
        .library(name: "Assets", targets: ["Assets"]),
        .library(name: "MapFeature", targets: ["MapFeature"]),
        .library(name: "Model", targets: ["Model"]),
        .library(name: "NotificationFeature", targets: ["NotificationFeature"]),
        .library(name: "SafariView", targets: ["SafariView"]),
        .library(name: "SearchFeature", targets: ["SearchFeature"]),
        .library(name: "SessionFeature", targets: ["SessionFeature"]),
        .library(name: "SettingFeature", targets: ["SettingFeature"]),
        .library(name: "Strings", targets: ["Strings"]),
        .library(name: "TimetableFeature", targets: ["TimetableFeature"]),
        .library(name: "Theme", targets: ["Theme"]),
    ],
    dependencies: [
        .package(url: "https://github.com/firebase/firebase-ios-sdk.git", from: "9.6.0"),
        .package(url: "https://github.com/pointfreeco/swift-composable-architecture", from: "0.40.2"),
        .package(url: "https://github.com/cybozu/LicenseList", from: "0.1.5"),
    ],
    targets: [
        .target(
            name: "AboutFeature",
            dependencies: [
                .target(name: "Strings"),
                .target(name: "Theme"),
                .target(name: "SafariView"),
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
                .product(name: "LicenseList", package: "LicenseList")
            ],
            resources: [
                .process("swiftgen.yml"),
                .process("Resources"),
                .process("license-list.plist"),
            ],
            plugins: [
                .plugin(name: "SwiftGenPlugin"),
                .plugin(name: "LicenseListPlugin")
            ]
        ),
        .target(
            name: "AppFeature",
            dependencies: [
                .target(name: "appioscombined"),
                .target(name: "AboutFeature"),
                .target(name: "Assets"),
                .target(name: "Auth"),
                .target(name: "Container"),
                .target(name: "ContributorFeature"),
                .target(name: "MapFeature"),
                .target(name: "NotificationFeature"),
                .target(name: "SponsorFeature"),
                .target(name: "Strings"),
                .target(name: "Theme"),
                .target(name: "SearchFeature"),
                .target(name: "SessionFeature"),
                .target(name: "SettingFeature"),
                .target(name: "TimetableFeature"),
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),
        .target(
            name: "Assets",
            resources: [
                .process("swiftgen.yml"),
                .process("Resources"),
            ],
            plugins: [
                .plugin(name: "SwiftGenPlugin"),
            ]
        ),
        .target(
            name: "Auth",
            dependencies: [
                .target(name: "appioscombined"),
                .product(name: "FirebaseAuth", package: "firebase-ios-sdk"),
            ]
        ),
        .target(
            name: "CommonComponents",
            dependencies: [
                .target(name: "Assets"),
                .target(name: "Model"),
                .target(name: "Theme"),
            ]
        ),
        .target(
            name: "Container",
            dependencies: [
                .target(name: "Auth"),
                .target(name: "appioscombined"),
            ]
        ),
        .target(
            name: "ContributorFeature",
            dependencies: [
                .target(name: "Assets"),
                .target(name: "Model"),
                .target(name: "Strings"),
                .target(name: "Theme"),
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),
        .target(
            name: "MapFeature",
            dependencies: [
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),
        .target(
            name: "Model",
            dependencies: [
                .target(name: "appioscombined"),
            ]
        ),
        .target(
            name: "NotificationFeature",
            dependencies: [
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),
        .target(
            name: "SafariView",
            dependencies: []
        ),
        .target(
            name: "SearchFeature",
            dependencies: [
                .target(name: "CommonComponents"),
                .target(name: "Model"),
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),
        .target(
            name: "SessionFeature",
            dependencies: [
                .target(name: "Model"),
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),
        .target(
            name: "SettingFeature",
            dependencies: [
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),
        .target(
            name: "SponsorFeature",
            dependencies: [
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),
        .target(
            name: "Strings",
            resources: [
                .process("swiftgen.yml"),
                .process("Resources"),
            ],
            plugins: [
                .plugin(name: "SwiftGenPlugin"),
            ]
        ),
        .target(
            name: "Theme",
            resources: [
                .process("swiftgen.yml"),
                .process("Resources"),
            ],
            plugins: [
                .plugin(name: "SwiftGenPlugin"),
            ]
        ),
        .target(
            name: "TimetableFeature",
            dependencies: [
                .target(name: "Assets"),
                .target(name: "CommonComponents"),
                .target(name: "Model"),
                .target(name: "Theme"),
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
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
        .plugin(
            name: "SwiftGenPlugin",
            capability: .buildTool(),
            dependencies: [
                .target(name: "swiftgen"),
            ]
        ),
        .plugin(
            name: "LicenseListPlugin",
            capability: .buildTool(),
            dependencies: [
                .target(name: "licenselist")
            ]
        ),
        .binaryTarget(
            name: "SwiftLintBinary",
            url: "https://github.com/realm/SwiftLint/releases/download/0.48.0/SwiftLintBinary-macos.artifactbundle.zip",
            checksum: "9c255e797260054296f9e4e4cd7e1339a15093d75f7c4227b9568d63edddba50"
        ),
        .binaryTarget(
          name: "swiftgen",
          url: "https://github.com/SwiftGen/SwiftGen/releases/download/6.6.2/swiftgen-6.6.2.artifactbundle.zip",
          checksum: "7586363e24edcf18c2da3ef90f379e9559c1453f48ef5e8fbc0b818fbbc3a045"
        ),
        .binaryTarget(
            name: "licenselist",
            url: "https://github.com/touyou/LicenseList/releases/download/0.1.5/licenselist.artifactbundle.zip",
            checksum: "02d1b096c60dd0a4f3ff67a6ec82d801c6a609867fc84aa9ad40d00b42395417"
        )
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
