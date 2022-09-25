// swift-tools-version: 5.7
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

var package = Package(
    name: "DroidKaigiPackage",
    defaultLocalization: "en",
    platforms: [
        .iOS(.v16),
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
        .library(name: "AnnouncementFeature", targets: ["AnnouncementFeature"]),
        .library(name: "SafariView", targets: ["SafariView"]),
        .library(name: "SearchFeature", targets: ["SearchFeature"]),
        .library(name: "SessionFeature", targets: ["SessionFeature"]),
        .library(name: "SponsorFeature", targets: ["SponsorFeature"]),
        .library(name: "StaffFeature", targets: ["StaffFeature"]),
        .library(name: "TimetableFeature", targets: ["TimetableFeature"]),
        .library(name: "Theme", targets: ["Theme"]),
        .plugin(name: "swiftlint", targets: ["SwiftLintCommandPlugin"]),
    ],
    dependencies: [
        .package(url: "https://github.com/firebase/firebase-ios-sdk.git", from: "9.6.0"),
        .package(url: "https://github.com/pointfreeco/swift-composable-architecture", from: "0.40.2"),
        .package(url: "https://github.com/cybozu/LicenseList", from: "0.1.5"),
        .package(url: "https://github.com/onevcat/Kingfisher", from: "7.3.2"),
    ],
    targets: [
        .target(
            name: "AboutFeature",
            dependencies: [
                .target(name: "appioscombined"),
                .target(name: "ContributorFeature"),
                .target(name: "Model"),
                .target(name: "Theme"),
                .target(name: "SafariView"),
                .target(name: "StaffFeature"),
                .target(name: "SponsorFeature"),
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
                .target(name: "AnnouncementFeature"),
                .target(name: "Assets"),
                .target(name: "Auth"),
                .target(name: "Container"),
                .target(name: "ContributorFeature"),
                .target(name: "Event"),
                .target(name: "MapFeature"),
                .target(name: "SponsorFeature"),
                .target(name: "Theme"),
                .target(name: "SearchFeature"),
                .target(name: "SessionFeature"),
                .target(name: "StaffFeature"),
                .target(name: "TimetableFeature"),
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),
        .target(
            name: "AnnouncementFeature",
            dependencies: [
                .target(name: "appioscombined"),
                .target(name: "Assets"),
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
                .target(name: "Model"),
                .target(name: "Theme"),
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
                .product(name: "Kingfisher", package: "Kingfisher"),
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
                .target(name: "CommonComponents"),
                .target(name: "Model"),
                .target(name: "appioscombined"),
                .target(name: "Theme"),
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),
        .target(
            name: "Event"
        ),
        .target(
            name: "MapFeature",
            dependencies: [
                .target(name: "Assets"),            
                .target(name: "CommonComponents"),
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
                .target(name: "Model"),
            ]
        ),
        .target(
            name: "Model",
            dependencies: [
                .target(name: "appioscombined"),
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
                .target(name: "Event"),
                .target(name: "SessionFeature"),
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),
        .target(
            name: "SessionFeature",
            dependencies: [
                .target(name: "appioscombined"),
                .target(name: "Assets"),
                .target(name: "CommonComponents"),
                .target(name: "Event"),
                .target(name: "Model"),
                .target(name: "Theme"),
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),
        .target(
            name: "SponsorFeature",
            dependencies: [
                .target(name: "Assets"),
                .target(name: "CommonComponents"),
                .target(name: "Model"),
                .target(name: "SafariView"),
                .target(name: "Theme"),
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
            ]
        ),
        .target(
            name: "StaffFeature",
            dependencies: [
                .target(name: "CommonComponents"),
                .target(name: "Model"),
                .target(name: "Theme"),
                .product(name: "ComposableArchitecture", package: "swift-composable-architecture"),
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
            name: "SwiftLintCommandPlugin",
            capability: .command(intent: .custom(verb: "swiftlint",
                                                 description: "Enforce Swift style and conventions")),
            dependencies: ["SwiftLintBinary"]),
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
        ),
//        .binaryTarget(
//            name: "gradle",
//            url: "https://services.gradle.org/distributions/gradle-7.5.1-bin.zip",
//            checksum: "f6b8596b10cce501591e92f229816aa4046424f3b24d771751b06779d58c8ec4"
//        ),
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
