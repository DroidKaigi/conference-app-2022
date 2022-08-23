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
                    "--path",
                    target.directory.string   // only lint the files in the target directory
                ],
                // environment: ["IN_PROCESS_SOURCEKIT": "YES"] // doesn't work in Xcode 13.3
                environment: [:]
            )
        ]
    }
}
