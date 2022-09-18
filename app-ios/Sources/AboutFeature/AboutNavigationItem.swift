import appioscombined

struct AboutNavigationItem {
    let image: ImageAsset
    let title: String
    let action: AboutAction

    static var items: [Self] {
        [
            AboutNavigationItem(
                image: AboutViewAssets.train,
                title: Res.strings().about_access.desc().localized(),
                action: .openAccess
            ),
            AboutNavigationItem(
                image: AboutViewAssets.person,
                title: Res.strings().about_staff.desc().localized(),
                action: .openStaffs
            ),
            AboutNavigationItem(
                image: AboutViewAssets.shield,
                title: Res.strings().about_privacy.desc().localized(),
                action: .openPrivacyPolicy
            ),
            AboutNavigationItem(
                image: AboutViewAssets.folder,
                title: Res.strings().about_license.desc().localized(),
                action: .openLicense
            ),
        ]
    }
}
