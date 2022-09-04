import SwiftUI
import ComposableArchitecture

public struct ContributorState: Equatable {
    public init() {}
}

public enum ContributorAction {}
public struct ContributorEnvironment {
    public init() {}
}
public let contributorReducer = Reducer<ContributorState, ContributorAction, ContributorEnvironment> { _, _, _ in
    return .none
}

public struct ContributorView: View {
    private let store: Store<ContributorState, ContributorAction>

    public init(store: Store<ContributorState, ContributorAction>) {
        self.store = store
    }

    public var body: some View {
        Text("TODO: Contributor")
    }
}

#if DEBUG
struct ContributorView_Previews: PreviewProvider {
    static var previews: some View {
        ContributorView(
            store: .init(
                initialState: .init(),
                reducer: .empty,
                environment: ContributorEnvironment()
            )
        )
    }
}
#endif

