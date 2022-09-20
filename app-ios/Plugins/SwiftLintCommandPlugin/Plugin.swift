import PackagePlugin
import Foundation

@main
struct SwiftLintCommandPlugin: CommandPlugin {
    func performCommand(context: PackagePlugin.PluginContext, arguments: [String]) async throws {
        let swiftlintPath = try context.tool(named: "swiftlint").path
        let swiftlint = URL(fileURLWithPath: swiftlintPath.string)
        var args = arguments
        args += [
            "lint",
            "--in-process-sourcekit", // alternative to the environment variable
        ]
        let process = try Process.run(swiftlint, arguments: args)
        process.waitUntilExit()
        if process.terminationReason == .exit && process.terminationStatus == 0 {
            print("Linting succeeded!")
        }
        else {
            Diagnostics.error("Linting error occured!")
        }
    }
}
