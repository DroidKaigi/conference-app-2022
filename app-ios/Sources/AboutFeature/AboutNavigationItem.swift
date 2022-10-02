import appioscombined

struct AboutNavigationItem {
    let image: ImageAsset
    let title: String
    let action: AboutAction
    let destination: AboutDestination?

    static var items: [Self] {
        [
            AboutNavigationItem(
                image: AboutViewAssets.train,
                title: StringsKt.shared.about_access.localized(),
                action: .openAccess,
                destination: nil
            ),
            AboutNavigationItem(
                image: AboutViewAssets.person,
                title: StringsKt.shared.about_staff.localized(),
                action: .openStaffs,
                destination: .staffs
            ),
            AboutNavigationItem(
                image: AboutViewAssets.people,
                title: StringsKt.shared.title_contributors.localized(),
                action: .openContributors,
                destination: .contributor
            ),
            AboutNavigationItem(
                image: AboutViewAssets.company,
                title: StringsKt.shared.title_sponsors.localized(),
                action: .openSponsors,
                destination: .sponsor
            ),
            AboutNavigationItem(
                image: AboutViewAssets.shield,
                title: StringsKt.shared.about_privacy.localized(),
                action: .openPrivacyPolicy,
                destination: nil
            ),
            AboutNavigationItem(
                image: AboutViewAssets.folder,
                title: StringsKt.shared.about_license.localized(),
                action: .openLicense,
                destination: .license
            ),
        ]
    }
}
