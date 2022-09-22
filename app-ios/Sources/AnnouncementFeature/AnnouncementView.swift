import appioscombined
import ComposableArchitecture
import Model
import SwiftUI

public struct AnnouncementState: Equatable {
    public var announcements: [AnnouncementsByDate] = []

    public init() {}
}
public enum AnnouncementAction {
    case load
    case fetched([AnnouncementsByDate])
}

public struct AnnouncementEnvironment {
    public let repository: AnnouncementsRepository

    public init(repository: AnnouncementsRepository) {
        // TODO: Replace FakeRepository with DataRepository
        self.repository = FakeAnnouncementsRepository()
    }
}
public let announcementReducer = Reducer<AnnouncementState, AnnouncementAction, AnnouncementEnvironment> { state, action, environment in
    let repository = environment.repository
    switch action {
    case .load:
        return Effect.run { @MainActor subscriber in
            var iterator: AsyncThrowingStream<[AnnouncementsByDate], Error>.Iterator = repository.announcements().stream().makeAsyncIterator()
            guard let response = try await iterator.next() else {
                return
            }
            subscriber.send(.fetched(response))
        } catch: { error, _ in
            print(error)
        }
    case .fetched(let list):
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
                        ForEach(viewStore.announcements.indices, id: \.self) { index in
                            let announcementsByDate = viewStore.announcements[index]
                            AnnouncementsByDateView(announcementsByDate: announcementsByDate)
                        }
                    }
                }
                .navigationTitle(StringsKt.shared.announcement_top_app_bar_title.desc().localized())
                .navigationBarTitleDisplayMode(.inline)
            }
            .onAppear {
                viewStore.send(.load)
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
                environment: AnnouncementEnvironment(repository: FakeAnnouncementsRepository())
            )
        )
    }
}
#endif
