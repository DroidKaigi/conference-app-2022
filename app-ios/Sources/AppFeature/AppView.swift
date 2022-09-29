import AboutFeature
import AnnouncementFeature
import appioscombined
import Assets
import Auth
import ComposableArchitecture
import Container
import Event
import MapFeature
import SearchFeature
import SessionFeature
import SwiftUI
import Theme
import TimetableFeature

public enum AppTab {
    case timetable
    case about
    case announcement
    case map
}

public struct AppState: Equatable {
    public var timetableState: TimetableState
    public var aboutState: AboutState
    public var announcementState: AnnouncementState
    public var mapState: MapState
    public var sessionState: SessionState?
    public var searchState: SearchState?
    public var selectedTab: AppTab

    public init(
        timetableState: TimetableState = .init(),
        aboutState: AboutState = .init(),
        announcementState: AnnouncementState = .init(),
        mapState: MapState = .init(),
        sessionState: SessionState? = nil,
        searchState: SearchState? = nil,
        selectedTab: AppTab = .timetable
    ) {
        self.timetableState = timetableState
        self.aboutState = aboutState
        self.announcementState = announcementState
        self.mapState = mapState
        self.searchState = searchState
        self.sessionState = sessionState
        self.selectedTab = selectedTab
    }
}

public enum AppAction {
    case timetable(TimetableAction)
    case about(AboutAction)
    case announcement(AnnouncementAction)
    case map(MapAction)
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
    public let announcementsRepository: AnnouncementsRepository
    public let staffRepository: StaffRepository
    public let eventKitClient: EventKitClientProtocol

    public init(
        contributorsRepository: ContributorsRepository,
        sponsorsRepository: SponsorsRepository,
        sessionsRepository: SessionsRepository,
        announcementsRepository: AnnouncementsRepository,
        staffRepository: StaffRepository,
        eventKitClient: EventKitClientProtocol
    ) {
        self.contributorsRepository = contributorsRepository
        self.sponsorsRepository = sponsorsRepository
        self.sessionsRepository = sessionsRepository
        self.announcementsRepository = announcementsRepository
        self.staffRepository = staffRepository
        self.eventKitClient = eventKitClient
    }
}

public extension AppEnvironment {
    static var client: Self {
        let container = DIContainer(authenticator: Auth.Authenticator())

        return .init(
            contributorsRepository: container.get(type: ContributorsRepository.self),
            sponsorsRepository: container.get(type: SponsorsRepository.self),
            sessionsRepository: container.get(type: SessionsRepository.self),
            announcementsRepository: container.get(type: AnnouncementsRepository.self),
            staffRepository: container.get(type: StaffRepository.self),
            eventKitClient: EventKitClient()
        )
    }

    static var mock: Self {
        return .init(
            contributorsRepository: FakeContributorsRepository(),
            sponsorsRepository: FakeSponsorsRepository(),
            sessionsRepository: FakeSessionsRepository(),
            announcementsRepository: FakeAnnouncementsRepository(),
            staffRepository: FakeStaffRepository(),
            eventKitClient: EventKitClientMock()
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
        environment: {
            .init(
                staffRepository: $0.staffRepository,
                contributorsRepository: $0.contributorsRepository,
                sponsorRepository: $0.sponsorsRepository
            )
        }
    ),
    announcementReducer.pullback(
        state: \.announcementState,
        action: /AppAction.announcement,
        environment: {
            .init(
                announcementsRepository: $0.announcementsRepository
            )
        }
    ),
    mapReducer.pullback(
        state: \.mapState,
        action: /AppAction.map,
        environment: { _ in
            .init()
        }
    ),
    searchReducer.optional().pullback(
        state: \.searchState,
        action: /AppAction.search,
        environment: {
            .init(
                sessionsRepository: $0.sessionsRepository,
                eventKitClient: $0.eventKitClient
            )
        }
    ),
    sessionReducer.optional().pullback(
        state: \.sessionState,
        action: /AppAction.session,
        environment: {
            .init(
                sessionsRepository: $0.sessionsRepository,
                eventKitClient: $0.eventKitClient
            )
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
        case .announcement:
            return .none
        case .map:
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

        UIToolbar.appearance().backgroundColor = AssetColors.surface.color
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
                        Text(StringsKt.shared.title_sessions.localized())
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
                    Text(StringsKt.shared.title_about.localized())
                }
                .tag(AppTab.about)
                AnnouncementView(
                    store: store.scope(
                        state: \.announcementState,
                        action: AppAction.announcement
                    )
                )
                .tabItem {
                    Assets.announcement.swiftUIImage
                        .renderingMode(.template)
                    Text(StringsKt.shared.title_announcement.localized())
                }
                .tag(AppTab.announcement)
                MapView(
                    store: store.scope(
                        state: \.mapState,
                        action: AppAction.map
                    )
                )
                .tabItem {
                    Assets.map.swiftUIImage
                        .renderingMode(.template)
                    Text(StringsKt.shared.title_map.localized())
                }
                .tag(AppTab.map)
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
