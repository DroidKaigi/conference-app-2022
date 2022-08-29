import AppFeature
import SwiftUI

@main
struct DroidKaigi2022App: App {
    var body: some Scene {
        WindowGroup {
            AppView(
                store: .init(
                    initialState: .init(),
                    reducer: appReducer,
                    environment: AppEnvironment()
                )
            )
        }
    }
}
