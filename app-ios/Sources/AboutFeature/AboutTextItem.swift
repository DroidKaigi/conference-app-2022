import appioscombined

struct AboutTextItem {
    let title: String
    let content: String

    private static let appVersion = Bundle.main.object(forInfoDictionaryKey: "CFBundleShortVersionString") as? String ?? ""

    static var items: [Self] {
        [
            AboutTextItem(
                title: Res.strings().about_app_version.desc().localized(),
                content: appVersion
            ),
        ]
    }
}
