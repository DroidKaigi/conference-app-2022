import SwiftUI
import ComposableArchitecture

public struct AboutState: Equatable {
    public init() {}
}
public enum AboutAction {}
public struct AboutEnvironment {
    public init() {}
}
public let aboutReducer = Reducer<AboutState, AboutAction, AboutEnvironment> { _, _, _ in
    return .none
}

public struct AboutView: View {
    private let store: Store<AboutState, AboutAction>

    public init(store: Store<AboutState, AboutAction>) {
        self.store = store
    }

    public var body: some View {
        Text("TODO: About")
    }
}

#if DEBUG
struct AboutView_Previews: PreviewProvider {
    static var previews: some View {
        AboutView(
            store: .init(
                initialState: .init(),
                reducer: .empty,
                environment: AboutEnvironment()
            )
        )
    }
}
#endif
