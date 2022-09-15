import Strings
import Foundation

struct AboutTextItem {
    let title: String
    let content: String

    private static let appVersion = Bundle.main.object(forInfoDictionaryKey: "CFBundleShortVersionString") as? String ?? ""

    static var items: [Self] {
        [
            AboutTextItem(
                title: L10n.About.version,
                content: appVersion
            ),
        ]
    }
}
