import SwiftUI
import ComposableArchitecture

public struct SponsorState: Equatable {
    public init() {}
}

public enum SponsorAction {}
public struct SponsorEnvironment {
    public init() {}
}
public let sponsorReducer = Reducer<SponsorState, SponsorAction, SponsorEnvironment> { _, _, _ in
    return .none
}

public struct SponsorView: View {
    private let store: Store<SponsorState, SponsorAction>

    public init(store: Store<SponsorState, SponsorAction>) {
        self.store = store
    }

    public var body: some View {
        Text("TODO: Sponsor")
    }
}

#if DEBUG
struct SponsorView_Previews: PreviewProvider {
    static var previews: some View {
        SponsorView(
            store: .init(
                initialState: .init(),
                reducer: .empty,
                environment: SponsorEnvironment()
            )
        )
    }
}
#endif

