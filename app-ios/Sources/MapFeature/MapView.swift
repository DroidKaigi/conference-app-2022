import ComposableArchitecture
import SwiftUI

public struct MapState: Equatable {
    public init() {}
}

public enum MapAction {}
public struct MapEnvironment {
    public init() {}
}
public let mapReducer = Reducer<MapState, MapAction, MapEnvironment> { _, _, _ in
    return .none
}

public struct MapView: View {
    private let store: Store<MapState, MapAction>

    public init(store: Store<MapState, MapAction>) {
        self.store = store
    }

    public var body: some View {
        Text("TODO: Map")
    }
}

#if DEBUG
struct MapView_Previews: PreviewProvider {
    static var previews: some View {
        MapView(
            store: .init(
                initialState: .init(),
                reducer: .empty,
                environment: MapEnvironment()
            )
        )
    }
}
#endif
