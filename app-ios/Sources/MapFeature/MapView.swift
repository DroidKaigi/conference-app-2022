import ComposableArchitecture
import SwiftUI
import Strings

public struct MapState: Equatable {
    // FIXME: This is a temporary image.
    let mapURL = URL(string: "https://user-images.githubusercontent.com/5885032/191032572-b128660f-bff2-4cd4-8228-27cc8f8974a9.png")

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
        WithViewStore(store) { viewStore in
            NavigationView {
                VStack(alignment: .center) {
                    AsyncImage(url: viewStore.mapURL)
                        .aspectRatio(contentMode: .fit)
                        .padding(16)
                }
                .navigationTitle(L10n.Map.title)
                .navigationBarTitleDisplayMode(.inline)
            }
        }
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
