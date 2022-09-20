import AboutFeature
import appioscombined
import Assets
import Auth
import ComposableArchitecture
import Container
import ContributorFeature
import MapFeature
import NotificationFeature
import SearchFeature
import SessionFeature
import SettingFeature
import SponsorFeature
import Strings
import SwiftUI
import Theme
import TimetableFeature

public enum AppTab {
    case timetable
    case about
    case notification
    case map
    case sponsor
    case contributor
    case setting
}

public struct AppState: Equatable {
    public var timetableState: TimetableState
    public var aboutState: AboutState
    public var notificationState: NotificationState
    public var mapState: MapState
    public var sponsorState: SponsorState
    public var contributorState: ContributorState
    public var settingState: SettingState
    public var sessionState: SessionState?
    public var searchState: SearchState?
    public var selectedTab: AppTab

    public init(
        timetableState: TimetableState = .init(),
        aboutState: AboutState = .init(),
        notificationState: NotificationState = .init(),
        mapState: MapState = .init(),
        sponsorState: SponsorState = .init(),
        contributorState: ContributorState = .init(),
        settingState: SettingState = .init(),
        sessionState: SessionState? = nil,
        searchState: SearchState? = nil,
        selectedTab: AppTab = .timetable
    ) {
        self.timetableState = timetableState
        self.aboutState = aboutState
        self.notificationState = notificationState
        self.mapState = mapState
        self.sponsorState = sponsorState
        self.contributorState = contributorState
        self.settingState = settingState
        self.searchState = searchState
        self.sessionState = sessionState
        self.selectedTab = selectedTab
    }
}

public enum AppAction {
    case timetable(TimetableAction)
    case about(AboutAction)
    case notification(NotificationAction)
    case map(MapAction)
    case sponsor(SponsorAction)
    case contributor(ContributorAction)
    case setting(SettingAction)
    case search(SearchAction)
    case session(SessionAction)
    case selectTab(AppTab)
    case hideSearchSheet
    case hideSessionSheet
}

public struct AppEnvironment {
    public let contributorsRepository: ContributorsRepository
    public let sponsorsRepository: SponsorsRepository
    public let sessionsRepository: SessionsRepository

    public init(
        contributorsRepository: ContributorsRepository,
        sponsorsRepository: SponsorsRepository,
        sessionsRepository: SessionsRepository
    ) {
        self.contributorsRepository = contributorsRepository
        self.sponsorsRepository = sponsorsRepository
        self.sessionsRepository = sessionsRepository
    }
}

public extension AppEnvironment {
    static var client: Self {
        let container = DIContainer(authenticator: Auth.Authenticator())

        return .init(
            contributorsRepository: container.get(type: ContributorsRepository.self),
            sponsorsRepository: container.get(type: SponsorsRepository.self),
            sessionsRepository: container.get(type: SessionsRepository.self)
        )
    }

    static var mock: Self {
        return .init(
            contributorsRepository: FakeContributorsRepository(),
            sponsorsRepository: FakeSponsorsRepository(),
            sessionsRepository: FakeSessionsRepository()
        )
    }
}

public let appReducer = Reducer<AppState, AppAction, AppEnvironment>.combine(
    timetableReducer.pullback(
        state: \.timetableState,
        action: /AppAction.timetable,
        environment: {
            .init(
                sessionsRepository: $0.sessionsRepository
            )
        }
    ),
    aboutReducer.pullback(
        state: \.aboutState,
        action: /AppAction.about,
        environment: { _ in
            .init()
        }
    ),
    notificationReducer.pullback(
        state: \.notificationState,
        action: /AppAction.notification,
        environment: { _ in
            .init()
        }
    ),
    mapReducer.pullback(
        state: \.mapState,
        action: /AppAction.map,
        environment: { _ in
            .init()
        }
    ),
    sponsorReducer.pullback(
        state: \.sponsorState,
        action: /AppAction.sponsor,
        environment: {
            .init(
                sponsorsRepository: $0.sponsorsRepository
            )
        }
    ),
    contributorReducer.pullback(
        state: \.contributorState,
        action: /AppAction.contributor,
        environment: {
            .init(
                contributorsRepository: $0.contributorsRepository
            )
        }
    ),
    settingReducer.pullback(
        state: \.settingState,
        action: /AppAction.setting,
        environment: { _ in
            .init()
        }
    ),
    searchReducer.optional().pullback(
        state: \.searchState,
        action: /AppAction.search,
        environment: {
            .init(
                sessionsRepository: $0.sessionsRepository
            )
        }
    ),
    sessionReducer.optional().pullback(
        state: \.sessionState,
        action: /AppAction.session,
        environment: {
            .init(sessionsRepository: $0.sessionsRepository)
        }
    ),
    .init { state, action, _ in
        switch action {
        case let .timetable(.selectItem(item)):
            state.sessionState = .init(
                timetableItemWithFavorite: item
            )
            return .none
        case .timetable(.search):
            state.searchState = .init()
            return .none
        case .timetable:
            return .none
        case .about:
            return .none
        case .notification:
            return .none
        case .map:
            return .none
        case .sponsor:
            return .none
        case .contributor:
            return .none
        case .setting:
            return .none
        case .search:
            return .none
        case .session:
            return .none
        case let .selectTab(tab):
            state.selectedTab = tab
            return .none
        case .hideSearchSheet:
            state.searchState = nil
            return .none
        case .hideSessionSheet:
            state.sessionState = nil
            return .none
        }
    }
)

public struct AppView: View {
    private let store: Store<AppState, AppAction>

    public init(store: Store<AppState, AppAction>) {
        self.store = store

        // workaround for tabbar colors. From iOS16, toolbarBackground may be useful
        UITabBar.appearance().barTintColor = AssetColors.surface.color

        // workaround for preventing translucent tabbar. From iOS15
        let tabBarAppearance: UITabBarAppearance = UITabBarAppearance()
        tabBarAppearance.backgroundColor = AssetColors.surface.color
        UITabBar.appearance().standardAppearance = tabBarAppearance
        UITabBar.appearance().scrollEdgeAppearance = tabBarAppearance

        // workaround for preventing translucent navigation bar. From iOS15
        let navigationBarAppearance = UINavigationBarAppearance()
        navigationBarAppearance.backgroundColor = AssetColors.surface.color
        navigationBarAppearance.shadowColor = .clear
        UINavigationBar.appearance().standardAppearance = navigationBarAppearance
        UINavigationBar.appearance().compactAppearance = navigationBarAppearance
        UINavigationBar.appearance().scrollEdgeAppearance = navigationBarAppearance
    }

    public var body: some View {
        WithViewStore(store) { viewStore in
            TabView(selection: viewStore.binding(
                get: \.selectedTab,
                send: { value in
                    .selectTab(value)
                })
            ) {
                TimetableView(
                    store: store.scope(
                        state: \.timetableState,
                        action: AppAction.timetable
                    )
                )
                .tabItem {
                    Label {
                        Text(L10n.Timetable.title)
                    } icon: {
                        Assets.calendar.swiftUIImage
                            .renderingMode(.template)
                    }
                }
                .tag(AppTab.timetable)
                AboutView(
                    store: store.scope(
                        state: \.aboutState,
                        action: AppAction.about
                    )
                )
                .tabItem {
                    Image(systemName: "questionmark.circle")
                    Text(L10n.About.title)
                }
                .tag(AppTab.about)
                NotificationView(
                    store: store.scope(
                        state: \.notificationState,
                        action: AppAction.notification
                    )
                )
                .tabItem {
                    Assets.notification.swiftUIImage
                        .renderingMode(.template)
                    Text(L10n.Notification.title)
                }
                .tag(AppTab.notification)
                MapView(
                    store: store.scope(
                        state: \.mapState,
                        action: AppAction.map
                    )
                )
                .tabItem {
                    Assets.map.swiftUIImage
                        .renderingMode(.template)
                    Text(L10n.Map.title)
                }
                .tag(AppTab.map)
                SponsorView(
                    store: store.scope(
                        state: \.sponsorState,
                        action: AppAction.sponsor
                    )
                )
                .tabItem {
                    Assets.company.swiftUIImage
                        .renderingMode(.template)
                    Text(L10n.Sponsor.title)
                }
                .tag(AppTab.sponsor)
                ContributorView(
                    store: store.scope(
                        state: \.contributorState,
                        action: AppAction.contributor
                    )
                )
                .tabItem {
                    Assets.people.swiftUIImage
                        .renderingMode(.template)
                    Text(L10n.Contributors.title)
                }
                .tag(AppTab.contributor)
                SettingView(
                    store: store.scope(
                        state: \.settingState,
                        action: AppAction.setting
                    )
                )
                .tabItem {
                    Assets.gear.swiftUIImage
                        .renderingMode(.template)
                    Text(L10n.Setting.title)
                }
                .tag(AppTab.setting)
            }
            .accentColor(AssetColors.onSurface.swiftUIColor)
            .sheet(
                isPresented: viewStore.binding(
                    get: { $0.searchState != nil },
                    send: { _ in .hideSearchSheet }
                ),
                onDismiss: {
                    viewStore.send(.hideSearchSheet)
                }, content: {
                    IfLetStore(
                        store.scope(
                            state: \.searchState,
                            action: AppAction.search
                        )
                    ) { searchStore in
                        SearchView(store: searchStore)
                    }
                }
            )
            .sheet(
                isPresented: viewStore.binding(
                    get: { $0.sessionState != nil },
                    send: { _ in .hideSessionSheet }
                ),
                onDismiss: {
                    viewStore.send(.hideSessionSheet)
                },
                content: {
                    IfLetStore(
                        store.scope(
                            state: \.sessionState,
                            action: AppAction.session)
                    ) { sessionStore in
                        SessionView(store: sessionStore)
                    }
                }
            )
        }
        .preferredColorScheme(.dark)
    }
}

#if DEBUG
struct AppView_Previews: PreviewProvider {
    static var previews: some View {
        AppView(
            store: .init(
                initialState: .init(),
                reducer: .empty,
                environment: AppEnvironment.mock
            )
        )
    }
}
#endif
