import appioscombined
import Assets
import ComposableArchitecture
import Model
import SwiftUI
import Theme

public struct MapState: Equatable {
    public init() {}
}

public enum MapAction: Equatable {
    case tapPin
}

public struct MapEnvironment {
    public init() {}
}

public let mapReducer = Reducer<MapState, MapAction, MapEnvironment> { _, action, _ in
    switch action {
    case .tapPin:
        print("TODO: Pin is not implemented yet!")
        return .none
    }
}

public struct MapView: View {
    private let store: Store<MapState, MapAction>

    public init(store: Store<MapState, MapAction>) {
        self.store = store
    }

    public var body: some View {
        WithViewStore(store) { viewStore in
            NavigationView {
                ZStack {
                    AssetColors.background.swiftUIColor
                    Image(asset: Assets.floorMap)
                        .resizable()
                        .scaledToFit()
                        .padding(14)
                        .navigationTitle(StringsKt.shared.title_map.localized())
                        .navigationBarTitleDisplayMode(.inline)
                        .toolbar {
                            ToolbarItem(placement: .bottomBar) {
                                Button(
                                    action: { viewStore.send(.tapPin) },
                                    label: { Image(asset: Assets.pin) }
                                )
                            }
                            ToolbarItem(placement: .bottomBar) {
                                Spacer()
                            }
                        }
                }
            }
            .navigationViewStyle(.stack)
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
