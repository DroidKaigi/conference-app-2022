## Get Started
### Setup
```sh
cd ../
./gradlew appioscombined:assembleXCFramework
```

### Open project with Xcode
```sh
 open DroidKaigi2022/DroidKaigi2022.xcodeproj
```

:warning: If you cloned for the first time, you need to run build `Assets` target, `AboutFeature` target, `Strings` target, and `Theme` target.
## Requirements
- Xcode 13.4.1 ([Workaround needed for build](#workaround))
- Xcode 14.0 or Later (Recommended)

## Source Code
### Architecture
[The Composable Architecture](https://github.com/pointfreeco/swift-composable-architecture)

### Project Structure
Feature based module definition.

## Development Tools
Development tools are introduced by Swift Package Manager Plugin.

- [SwiftLint](https://github.com/realm/SwiftLint)
- [SwiftGen](https://github.com/SwiftGen/SwiftGen)

In this project, these packages are local plugin (Local defined plugin) and the why of using local plugin is performance issue of Xcode. When using remote package plugin, Xcode CPU usage becomes so high (over 100%).

### Code sample

```swift
import PackagePlugin

@main
struct SwiftLintPlugins: BuildToolPlugin {
    func createBuildCommands(context: PluginContext, target: Target) async throws -> [Command] {
        return [
            .buildCommand(
                displayName: "Linting \(target.name)",
                executable: try context.tool(named: "swiftlint").path,
                arguments: [
                    "lint",
                    "--in-process-sourcekit", // alternative to the environment variable
                    target.directory.string   // only lint the files in the target directory
                ],
                environment: [:]
            )
        ]
    }
}

```

## Workaround
### Xcode 13.4.1

For building this App with Xcode 13.4.1, we need to run following before build whole app.
Some targets that depends on SwiftGenPlugin needs to build individualy.

For example, a target "Theme" depends on "SwiftGenPlugin", it needs to build individualy.

### Xcode Previews

Before preview resume, specifying "Target" that a preview target view is belonged is needed.

For example, when preview "AboutView", specify "AboutFeature".
