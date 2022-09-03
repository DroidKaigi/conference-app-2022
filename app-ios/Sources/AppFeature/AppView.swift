import ComposableArchitecture
import SwiftUI
import Strings
import TimetableFeature
import SessionFeature

public enum AppTab {
    case timetable
}

public struct AppState: Equatable {
    public var timetableState: TimetableState
    public var sessionState: SessionState?
    public var selectedTab: AppTab

    public init(
        timetableState: TimetableState = .init(),
        sessionState: SessionState? = nil,
        selectedTab: AppTab = .timetable
    ) {
        self.timetableState = timetableState
        self.sessionState = sessionState
        self.selectedTab = selectedTab
    }
}

public enum AppAction {
    case timetable(TimetableAction)
    case session(SessionAction)
    case selectTab(AppTab)
    case hideSessionSheet
}

public struct AppEnvironment {
    public init() {}
}

public let appReducer = Reducer<AppState, AppAction, AppEnvironment>.combine(
    timetableReducer.pullback(
        state: \.timetableState,
        action: /AppAction.timetable,
        environment: { _ in
            .init()
        }
    ),
    sessionReducer.optional().pullback(
        state: \.sessionState,
        action: /AppAction.session,
        environment: {_ in
            .init()
        }
    ),
    .init { state, action, _ in
        switch action {
        case let .timetable(.selectItem(item)):
            state.sessionState = .init(
                timetableItem: item
            )
            return .none
        case .timetable:
            return .none
        case .session:
            return .none
        case .session:
            return .none
        case let .selectTab(tab):
            state.selectedTab = tab
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
                    Label(L10n.Timetable.title, systemImage: "questionmark.circle")
                }
            }
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
    }
}

#if DEBUG
struct AppView_Previews: PreviewProvider {
    static var previews: some View {
        AppView(
            store: .init(
                initialState: .init(),
                reducer: .empty,
                environment: AppEnvironment()
            )
        )
    }
}
#endif
