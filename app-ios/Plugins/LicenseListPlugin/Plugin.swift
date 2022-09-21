import Foundation
import PackagePlugin

@main
struct LicenseListPlugin: BuildToolPlugin {
    func createBuildCommands(context: PluginContext, target: Target) async throws -> [Command] {
        let regex = try NSRegularExpression(pattern: ".*SourcePackages")
        let pluginWorkDirectory = context.pluginWorkDirectory.string
        guard let matchRange = regex.firstMatch(in: pluginWorkDirectory, range: NSRange(0..<pluginWorkDirectory.count))?.range(at: 0) else {
            print("Failed match")
            return []
        }
        let sourcePackageDirectory = NSString(string: pluginWorkDirectory).substring(with: matchRange)
        
        return [
            .prebuildCommand(
                displayName: "LicenseList",
                executable: try context.tool(named: "spp").path,
                arguments: [
                    target.directory.string,
                    sourcePackageDirectory,
                ],
                environment: [:],
                outputFilesDirectory: context.pluginWorkDirectory
            )
        ]
    }
}
