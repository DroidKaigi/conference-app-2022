import appioscombined
import ComposableArchitecture
import Model
import SwiftUI

public struct AnnouncementState: Equatable {
    public var announcements: [AnnouncementsByDate] = []

    public init() {}
}
public enum AnnouncementAction {
    case refresh
    case refreshResponse([AnnouncementsByDate])
}

public struct AnnouncementEnvironment {
    public let announcementsRepository: AnnouncementsRepository

    public init(announcementsRepository: AnnouncementsRepository) {
        // TODO: Replace FakeRepository with DataRepository
        self.announcementsRepository = FakeAnnouncementsRepository()
    }
}
public let announcementReducer = Reducer<AnnouncementState, AnnouncementAction, AnnouncementEnvironment> { state, action, environment in
    let repository = environment.announcementsRepository
    switch action {
    case .refresh:
        return .run { @MainActor subscriber in
            for try await response: [AnnouncementsByDate] in repository.announcements().stream() {
                subscriber.send(.refreshResponse(response))
            }
        }
    case .refreshResponse(let list):
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
                ScrollView {
                    LazyVStack(spacing: 0) {
                        ForEach(viewStore.announcements, id: \.publishedAt) { announcementsByDate in
                            AnnouncementsByDateView(announcementsByDate: announcementsByDate)
                        }
                    }
                }
                .navigationTitle(StringsKt.shared.announcement_top_app_bar_title.desc().localized())
                .navigationBarTitleDisplayMode(.inline)
            }
            .onAppear {
                viewStore.send(.refresh)
            }
        }
    }
}

#if DEBUG
struct AnnouncementView_Previews: PreviewProvider {
    static var previews: some View {
        AnnouncementView(
            store: .init(
                initialState: .init(),
                reducer: .empty,
                environment: AnnouncementEnvironment(announcementsRepository: FakeAnnouncementsRepository())
            )
        )
    }
}
#endif
