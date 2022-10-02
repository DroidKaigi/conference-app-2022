import appioscombined
import ComposableArchitecture
import ContributorFeature
import LicenseList
import SponsorFeature
import StaffFeature
import SwiftUI
import Theme

public enum AboutDestination: Hashable {
    case staffs
    case license
    case contributor
    case sponsor
}

public struct AboutState: Equatable {
    public var staffState: StaffState
    public var contributorState: ContributorState
    public var sponsorState: SponsorState

    public init(
        staffState: StaffState = .init(),
        contributorState: ContributorState = .init(),
        sponsorState: SponsorState = .init()
    ) {
        self.staffState = staffState
        self.contributorState = contributorState
        self.sponsorState = sponsorState
    }
}

public enum AboutAction {
    case openAccess
    case openStaffs
    case openContributors
    case openSponsors
    case openPrivacyPolicy
    case openLicense
    case staff(StaffAction)
    case contributor(ContributorAction)
    case sponsor(SponsorAction)
}

public struct AboutEnvironment {
    @Environment(\.openURL) var openURL
    public let staffRepository: StaffRepository
    public let contributorsRepository: ContributorsRepository
    public let sponsorRepository: SponsorsRepository

    public init(
        staffRepository: StaffRepository,
        contributorsRepository: ContributorsRepository,
        sponsorRepository: SponsorsRepository
    ) {
        self.staffRepository = staffRepository
        self.contributorsRepository = contributorsRepository
        self.sponsorRepository = sponsorRepository
    }
}

public let aboutReducer = Reducer<AboutState, AboutAction, AboutEnvironment>.combine(
    staffReducer.pullback(
        state: \.staffState,
        action: /AboutAction.staff,
        environment: {
            .init(
                staffRepository: $0.staffRepository
            )
        }
    ),
    contributorReducer.pullback(
        state: \.contributorState,
        action: /AboutAction.contributor,
        environment: {
            .init(
                contributorsRepository: $0.contributorsRepository
            )
        }
    ),
    sponsorReducer.pullback(
        state: \.sponsorState,
        action: /AboutAction.sponsor,
        environment: {
            .init(
                sponsorsRepository: $0.sponsorRepository
            )
        }
    ),
    .init { _, action, environment in
        switch action {
        case .openLicense:
            return .none
        case .openStaffs:
            return .none
        case .openContributors:
            return .none
        case .openSponsors:
            return .none
        case .openAccess:
            environment.openURL(URL(string: StaticURLs.access)!)
            return .none
        case .openPrivacyPolicy:
            environment.openURL(URL(string: StaticURLs.privacyPolicy)!)
            return .none
        case .staff:
            return .none
        case .contributor:
            return .none
        case .sponsor:
            return .none
        }
    }
)

public struct AboutView: View {
    private let store: Store<AboutState, AboutAction>

    public init(store: Store<AboutState, AboutAction>) {
        self.store = store
    }

    public var body: some View {
        WithViewStore(store) { viewStore in
            NavigationStack {
                ScrollView {
                    AboutViewAssets.logoCharacter.swiftUIImage
                    VStack(alignment: .leading, spacing: 24) {
                        Text(StringsKt.shared.about_title.localized())
                            .font(Font.system(size: 32, weight: .medium))
                        Text(StringsKt.shared.about_description.localized())
                        HStack(spacing: 16) {
                            LinkImage(
                                image: AboutViewAssets.twitter.swiftUIImage,
                                url: URL(string: StaticURLs.twitter)!
                            )
                            LinkImage(
                                image: AboutViewAssets.youtube.swiftUIImage,
                                url: URL(string: StaticURLs.youtube)!
                            )
                            LinkImage(
                                image: AboutViewAssets.medium.swiftUIImage,
                                url: URL(string: StaticURLs.medium)!
                            )
                            Spacer()
                        }
                    }
                    .padding(.horizontal, 32)
                    .padding(.vertical, 32)

                    Divider()
                        .background(AssetColors.outline.swiftUIColor)
                        .padding(.horizontal, 45)

                    ForEach(AboutNavigationItem.items, id: \.title) { item in
                        if let destination = item.destination {
                            NavigationLink(value: destination, label: {
                                AboutNavigationItemView(image: item.image.swiftUIImage, title: item.title)
                            })
                        } else {
                            Button {
                                viewStore.send(item.action)
                            } label: {
                                AboutNavigationItemView(image: item.image.swiftUIImage, title: item.title)
                            }
                        }
                    }
                    .padding(.horizontal, 29)

                    ForEach(AboutTextItem.items, id: \.title) { item in
                        HStack {
                            Text(item.title)
                            Spacer()
                            Text(item.content)
                            Spacer()
                                .frame(width: 14)
                        }
                        .padding(16)
                        .frame(minHeight: 56)
                    }
                    .padding(.horizontal, 29)

                    Spacer()
                        .frame(height: 32)
                }
                .foregroundColor(AssetColors.onBackground.swiftUIColor)
                .background(AssetColors.background.swiftUIColor)
                .navigationBarHidden(true)
                .navigationDestination(for: AboutDestination.self) { destination in
                    switch destination {
                    case .staffs:
                        StaffView(
                            store: store.scope(state: \.staffState, action: AboutAction.staff)
                        )
                    case .license:
                        AboutLicenseView()
                    case .contributor:
                        ContributorView(
                            store: store.scope(state: \.contributorState, action: AboutAction.contributor)
                        )
                    case .sponsor:
                        SponsorView(
                            store: store.scope(
                                state: \.sponsorState,
                                action: AboutAction.sponsor
                            )
                        )
                    }
                }
            }
        }
    }
}

#if DEBUG
struct AboutView_Previews: PreviewProvider {
    static var previews: some View {
        AboutView(
            store: .init(
                initialState: .init(),
                reducer: .empty,
                environment: AboutEnvironment(
                    staffRepository: FakeStaffRepository(),
                    contributorsRepository: FakeContributorsRepository(),
                    sponsorRepository: FakeSponsorsRepository()
                )
            )
        )
        .preferredColorScheme(.dark)
    }
}
#endif
