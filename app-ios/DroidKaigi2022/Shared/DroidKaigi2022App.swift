import App
import SwiftUI

@main
struct DroidKaigi2022App: App {
    var body: some Scene {
        WindowGroup {
            TimetableView(timetable: .companion.fake())
        }
    }
}
