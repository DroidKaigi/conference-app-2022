import appioscombined
import Assets
import ComposableArchitecture
import Model
import SwiftUI
import Theme

public struct MapState: Equatable {
    public init() {}
}

public enum MapAction: Equatable {}

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
        WithViewStore(store) { _ in
            NavigationStack {
                ZStack {
                    AssetColors.background.swiftUIColor
                    Image(asset: Assets.floorMap)
                        .resizable()
                        .scaledToFit()
                        .padding(14)
                }
                .navigationTitle(StringsKt.shared.title_map.localized())
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
