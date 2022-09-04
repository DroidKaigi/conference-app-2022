import ComposableArchitecture
import SwiftUI

public struct NotificationState: Equatable {
    public init() {}
}
public enum NotificationAction {}
public struct NotificationEnvironment {
    public init() {}
}
public let notificationReducer = Reducer<NotificationState, NotificationAction, NotificationEnvironment> { _, _, _ in
    return .none
}

public struct NotificationView: View {
    private let store: Store<NotificationState, NotificationAction>

    public init(store: Store<NotificationState, NotificationAction>) {
        self.store = store
    }

    public var body: some View {
        Text("TODO: Notification")
    }
}

#if DEBUG
struct NotificationView_Previews: PreviewProvider {
    static var previews: some View {
        NotificationView(
            store: .init(
                initialState: .init(),
                reducer: .empty,
                environment: NotificationEnvironment()
            )
        )
    }
}
#endif
