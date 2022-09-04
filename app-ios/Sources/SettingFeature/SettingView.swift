import SwiftUI
import ComposableArchitecture

public struct SettingState: Equatable {
    public init() {}
}

public enum SettingAction {}
public struct SettingEnvironment {
    public init() {}
}
public let settingReducer = Reducer<SettingState, SettingAction, SettingEnvironment> { _, _, _ in
    return .none
}

public struct SettingView: View {
    private let store: Store<SettingState, SettingAction>

    public init(store: Store<SettingState, SettingAction>) {
        self.store = store
    }

    public var body: some View {
        Text("TODO: Setting")
    }
}

#if DEBUG
struct SettingView_Previews: PreviewProvider {
    static var previews: some View {
        SettingView(
            store: .init(
                initialState: .init(),
                reducer: .empty,
                environment: SettingEnvironment()
            )
        )
    }
}
#endif

