import ComposableArchitecture
import SwiftUI
import Strings
import TimetableFeature

public enum AppTab {
    case timetable
}

public struct AppState: Equatable {
    public var timetableState: TimetableState
    public var selectedTab: AppTab

    public init(
        timetableState: TimetableState = .init(),
        selectedTab: AppTab = .timetable
    ) {
        self.timetableState = timetableState
        self.selectedTab = selectedTab
    }
}

public enum AppAction {
    case timetable(TimetableAction)
    case selectTab(AppTab)
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
    .init({ state, action, _ in
        switch action {
        case .timetable:
            return .none
        case let .selectTab(tab):
            state.selectedTab = tab
            return .none
        }
    })
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
