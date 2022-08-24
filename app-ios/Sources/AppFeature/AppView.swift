import SwiftUI
import TimetableFeature

public struct AppView: View {
    enum Tab {
        case timetable
    }

    @State var selectedTab: Tab = .timetable

    public init() {}

    public var body: some View {
        TabView(selection: $selectedTab) {
            TimetableView(timetable: .companion.fake())
                .tabItem {
                    Label("Timetable", systemImage: "questionmark.circle")
                }
        }
    }
}

#if DEBUG
struct AppView_Previews: PreviewProvider {
    static var previews: some View {
        AppView()
    }
}
#endif
