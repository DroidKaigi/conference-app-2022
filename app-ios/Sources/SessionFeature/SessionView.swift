import ComposableArchitecture
import Model
import SwiftUI

public struct SessionState: Equatable {
    public var timetableItem: TimetableItem

    public init(timetableItem: TimetableItem) {
        self.timetableItem = timetableItem
    }
}

public enum SessionAction {
}

public struct SessionEnvironment {
    public init() {}
}

public let sessionReducer = Reducer<SessionState, SessionAction, SessionEnvironment> { _, action, _ in

    switch action {
    default:
        return .none
    }
}

public struct SessionView: View {
    private let store: Store<SessionState, SessionAction>

    public init(store: Store<SessionState, SessionAction>) {
        self.store = store
    }

    public var body: some View {
        Text("")
    }
}

#if DEBUG
struct SessionView_Previews: PreviewProvider {
    static var previews: some View {
        SessionView(
            store: .init(
                initialState: .init(
                    timetableItem: Timetable.companion.fake().timetableItems.first!
                ),
                reducer: .empty,
                environment: SessionEnvironment()
            )
        )
    }
}
#endif
