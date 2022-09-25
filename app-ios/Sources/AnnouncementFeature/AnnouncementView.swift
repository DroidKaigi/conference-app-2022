import appioscombined
import ComposableArchitecture
import Model
import SwiftUI
import Theme

public struct AnnouncementState: Equatable {
    public var announcements: [AnnouncementsByDate]
    public var isLoaded: Bool = false

    public init(announcements: [AnnouncementsByDate] = []) {
        self.announcements = announcements
    }
}
public enum AnnouncementAction {
    case refresh
    case refreshResponse([AnnouncementsByDate])
}

public struct AnnouncementEnvironment {
    public let announcementsRepository: AnnouncementsRepository

    public init(announcementsRepository: AnnouncementsRepository) {
        self.announcementsRepository = announcementsRepository
    }
}
public let announcementReducer = Reducer<AnnouncementState, AnnouncementAction, AnnouncementEnvironment> { state, action, environment in
    let repository = environment.announcementsRepository
    switch action {
    case .refresh:
        return .run { @MainActor subscriber in
            try await repository.refresh()
            for try await response: [AnnouncementsByDate] in repository.announcements().stream() {
                subscriber.send(.refreshResponse(response))
            }
        }
    case .refreshResponse(let list):
        state.isLoaded = true
        state.announcements = list
        return .none
    }
}

public struct AnnouncementView: View {
    private let store: Store<AnnouncementState, AnnouncementAction>

    public init(store: Store<AnnouncementState, AnnouncementAction>) {
        self.store = store
    }

    public var body: some View {
        WithViewStore(store) { viewStore in
            NavigationView {
                ZStack {
                    ColorsKt.shared.background.color()

                    if !viewStore.isLoaded {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: ColorsKt.shared.onBackground.color()))
                            .scaleEffect(x: 2, y: 2)
                    } else if viewStore.announcements.isEmpty {
                        empty()
                    } else {
                        fulfill(viewStore)
                    }
                }
                .navigationTitle(StringsKt.shared.announcement_top_app_bar_title.localized())
                .navigationBarTitleDisplayMode(.inline)
            }
            .task {
                if !viewStore.isLoaded {
                    viewStore.send(.refresh)
                }
            }
        }
    }

    func fulfill(_ viewStore: ViewStore<AnnouncementState, AnnouncementAction>) -> some View {
        ScrollView {
            LazyVStack(spacing: 0) {
                ForEach(viewStore.announcements, id: \.publishedAt) { announcementsByDate in
                    AnnouncementsByDateView(announcementsByDate: announcementsByDate)
                }
            }
        }
    }

    func empty() -> some View {
        VStack {
            Text(StringsKt.shared.announcement_content_empty.localized())
                .font(.system(size: 16, weight: .semibold))

        }
    }
}

#if DEBUG
struct AnnouncementView_Previews: PreviewProvider {
    static var previews: some View {
        AnnouncementView(
            store: .init(
                initialState: .init(
                    announcements: AnnouncementsByDate.companion.fakes()
                ),
                reducer: .empty,
                environment: AnnouncementEnvironment(announcementsRepository: FakeAnnouncementsRepository())
            )
        )
    }
}
#endif
