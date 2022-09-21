import CommonComponents
import ComposableArchitecture
import Model
import SwiftUI

public struct SearchState: Equatable {
    public var searchText: String
    public var dayToTimetable: [DroidKaigi2022Day: Timetable]
    public var searchResult: [DroidKaigi2022Day: Timetable]

    public init(
        searchText: String = "",
        dayToTimetable: [DroidKaigi2022Day: Timetable] = [:]
    ) {
        self.searchText = searchText
        self.dayToTimetable = dayToTimetable
        self.searchResult = dayToTimetable
    }
}

public enum SearchAction {
    case refresh
    case refreshResponse(TaskResult<[DroidKaigi2022Day: Timetable]>)
    case setFavorite(TimetableItemId, Bool)
    case setSearchText(String)
}

public struct SearchEnvironment {
    public let sessionsRepository: SessionsRepository

    public init(
        sessionsRepository: SessionsRepository
    ) {
        self.sessionsRepository = sessionsRepository
    }
}

public let searchReducer = Reducer<SearchState, SearchAction, SearchEnvironment> { state, action, environment in
    switch action {
    case .refresh:
        return .run { @MainActor subscriber in
            for try await droidKaigiSchedule: DroidKaigiSchedule in environment.sessionsRepository.droidKaigiScheduleFlow().stream() {
                await subscriber.send(
                    .refreshResponse(
                        TaskResult {
                            droidKaigiSchedule.dayToTimetable
                        }
                    )
                )
            }
        }
    case let .refreshResponse(.success(dayToTimetable)):
        state.dayToTimetable = dayToTimetable
        state.searchResult = dayToTimetable
        return .none
    case .refreshResponse:
        return .none
    case let .setFavorite(id, currentIsFavorite):
        return .run { @MainActor _ in
            try await environment.sessionsRepository.setFavorite(sessionId: id, favorite: !currentIsFavorite)
        }
        .receive(on: DispatchQueue.main.eraseToAnyScheduler())
        .eraseToEffect()
    case let .setSearchText(searchText):
        state.searchText = searchText
        state.searchResult = state.dayToTimetable.mapValues { timetable in
            Timetable(
                timetableItems: timetable.timetableItems.filter { item in
                    state.searchText.isEmpty
                    || item.title.jaTitle.contains(state.searchText)
                    || item.title.enTitle.contains(state.searchText)
                },
                favorites: timetable.favorites
            )
        }
        return .none
    }
}

public struct SearchView: View {
    private let store: Store<SearchState, SearchAction>

    public init(store: Store<SearchState, SearchAction>) {
        self.store = store
    }

    public var body: some View {
        WithViewStore(store) { viewStore in
            NavigationView {
                Group {
                    if viewStore.searchResult.values.allSatisfy(\.timetableItems.isEmpty) {
                        EmptyResultView()
                    } else {
                        List {
                            ForEach([DroidKaigi2022Day].fromKotlinArray(DroidKaigi2022Day.values())
                            ) { day in
                                Section(header: Text("\(day)")) {
                                    ForEach(viewStore.searchResult[day]?.contents ?? [], id: \.timetableItem.id.value) { timetableItem in
                                        HStack(alignment: .top, spacing: 33) {
                                            SearchedSessionTimeView(
                                                startsAt: timetableItem.timetableItem.startsAt.toDate(),
                                                endsAt: timetableItem.timetableItem.endsAt.toDate()
                                            )
                                            TimetableListItemView(
                                                item: timetableItem.timetableItem,
                                                isFavorite: timetableItem.isFavorited,
                                                searchText: viewStore.searchText
                                            ) { id, isFavorited in
                                                viewStore.send(.setFavorite(id, isFavorited))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                .searchable(text: viewStore.binding(
                    get: \.searchText,
                    send: { searchText in
                        .setSearchText(searchText)
                    })
                )
            }
            .task {
                await viewStore.send(.refresh).finish()
            }
        }
    }
}

#if DEBUG
struct SearchView_Previews: PreviewProvider {
    static var previews: some View {
        SearchView(
            store: .init(
                initialState: .init(
                    dayToTimetable: DroidKaigiSchedule.companion.fake().dayToTimetable
                ),
                reducer: .empty,
                environment: SearchEnvironment(
                    sessionsRepository: FakeSessionsRepository()
                )
            )
        )
    }
}
#endif
