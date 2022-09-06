struct AboutNavigationItem {
    let image: ImageAsset
    let title: String
    let action: AboutAction

    static var items: [Self] {
        [
            AboutNavigationItem(
                image: Assets.train,
                title: L10n.About.access,
                action: .openAccess
            ),
            AboutNavigationItem(
                image: Assets.person,
                title: L10n.About.staffs,
                action: .openStaffs
            ),
            AboutNavigationItem(
                image: Assets.shield,
                title: L10n.About.privacyPolicy,
                action: .openPrivacyPolicy
            ),
            AboutNavigationItem(
                image: Assets.folder,
                title: L10n.About.license,
                action: .openLicense
            ),
        ]
    }
}
