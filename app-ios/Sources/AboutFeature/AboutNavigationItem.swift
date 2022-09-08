import Strings

struct AboutNavigationItem {
    let image: ImageAsset
    let title: String
    let action: AboutAction

    static var items: [Self] {
        [
            AboutNavigationItem(
                image: AboutViewAssets.train,
                title: L10n.About.access,
                action: .openAccess
            ),
            AboutNavigationItem(
                image: AboutViewAssets.person,
                title: L10n.About.staffs,
                action: .openStaffs
            ),
            AboutNavigationItem(
                image: AboutViewAssets.shield,
                title: L10n.About.privacyPolicy,
                action: .openPrivacyPolicy
            ),
            AboutNavigationItem(
                image: AboutViewAssets.folder,
                title: L10n.About.license,
                action: .openLicense
            ),
        ]
    }
}
