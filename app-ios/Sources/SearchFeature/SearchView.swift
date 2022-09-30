import appioscombined
import CommonComponents
import ComposableArchitecture
import Event
import Model
import SessionFeature
import SwiftUI

extension Optional where Wrapped: DroidKaigi2022Day {
    func toArray() -> [DroidKaigi2022Day] {
        guard let self = self else { return [] }
        return [self]
    }
}

public struct SearchState: Equatable {
    public var searchText: String
    public var eventDays: [DroidKaigi2022Day]
    public var categories: [TimetableCategory]
    public var dayToTimetable: [DroidKaigi2022Day: Timetable]
    public var searchResult: [DroidKaigi2022Day: Timetable]
    public var sessionState: SessionState?
    public var showDayFilterSheet: Bool
    public var showCategoryFilterSheet: Bool
    public var filterFavorite: Bool
    public var filterDay: DroidKaigi2022Day?
    public var filterCategories: [TimetableCategory]

    public init(
        searchText: String = "",
        eventdays: [DroidKaigi2022Day] = [DroidKaigi2022Day].fromKotlinArray(DroidKaigi2022Day.values()),
        categories: [TimetableCategory] = [],
        dayToTimetable: [DroidKaigi2022Day: Timetable] = [:],
        sessionState: SessionState? = nil,
        showDayFilterSheet: Bool = false,
        showCategoryFilterSheet: Bool = false,
        filterFavorite: Bool = false,
        filterDay: DroidKaigi2022Day? = nil,
        filterCategories: [TimetableCategory] = []
    ) {
        self.searchText = searchText
        self.eventDays = eventdays
        self.categories = categories
        self.dayToTimetable = dayToTimetable
        self.searchResult = dayToTimetable
        self.sessionState = sessionState
        self.showDayFilterSheet = showDayFilterSheet
        self.showCategoryFilterSheet = showCategoryFilterSheet
        self.filterFavorite = filterFavorite
        self.filterDay = filterDay
        self.filterCategories = filterCategories
    }
}

public enum SearchAction {
    case refresh
    case refreshResponse(TaskResult<[DroidKaigi2022Day: Timetable]>)
    case setFavorite(TimetableItemId, Bool)
    case setSearchText(String)
    case setCategories([TimetableCategory])
    case selectItem(TimetableItemWithFavorite)
    case hideSessionSheet
    case session(SessionAction)
    case fetchCategories
    case selectCategory(TimetableCategory)
    case deselectCategory(TimetableCategory)
    case selectDay(DroidKaigi2022Day)
    case showDayFilterSheet
    case showCategoryFilterSheet
    case filterFavorite
    case hideDayFilterSheet
    case hideCategoryFilterSheet
}

public struct SearchEnvironment {
    public let sessionsRepository: SessionsRepository
    public let eventKitClient: EventKitClientProtocol

    public init(
        sessionsRepository: SessionsRepository,
        eventKitClient: EventKitClientProtocol
    ) {
        self.sessionsRepository = sessionsRepository
        self.eventKitClient = eventKitClient
    }
}

public let searchReducer = Reducer<SearchState, SearchAction, SearchEnvironment>.combine(
    sessionReducer.optional().pullback(
        state: \.sessionState,
        action: /SearchAction.session,
        environment: {
            .init(
                sessionsRepository: $0.sessionsRepository,
                eventKitClient: $0.eventKitClient
            )
        }
    ),
    .init { state, action, environment in
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
        case .fetchCategories:
            return .run { @MainActor subscriber in
                let categories = try await environment.sessionsRepository.getCategories()
                subscriber.send(.setCategories(categories))
            }
        case .setCategories(let categories):
            state.categories = categories
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
                timetable.filtered(
                    filters: Filters(
                        days: state.filterDay.toArray(),
                        categories: state.filterCategories,
                        filterFavorite: state.filterFavorite,
                        filterSession: false,
                        searchWord: state.searchText
                    )
                )
            }
            return .none
        case .selectItem(let item):
            state.sessionState = .init(timetableItemWithFavorite: item)
            return .none
        case .hideSessionSheet:
            state.sessionState = nil
            return .none
        case .session:
            return .none
        case .selectCategory(let category):
            state.filterCategories.append(category)
            return .none
        case .deselectCategory(let category):
            let index = state.filterCategories.firstIndex(of: category)
            guard let index = index else { return .none }
            state.filterCategories.remove(at: index)
            return .none
        case .selectDay(let day):
            state.filterDay = day
            state.showDayFilterSheet = false
            state.searchResult = state.dayToTimetable.mapValues { timetable in
                timetable.filtered(
                    filters: Filters(
                        days: state.filterDay.toArray(),
                        categories: state.filterCategories,
                        filterFavorite: state.filterFavorite,
                        filterSession: false,
                        searchWord: state.searchText
                    )
                )
            }
            return .none
        case .filterFavorite:
            state.filterFavorite.toggle()
            state.searchResult = state.dayToTimetable.mapValues { timetable in
                timetable.filtered(
                    filters: Filters(
                        days: state.filterDay.toArray(),
                        categories: state.filterCategories,
                        filterFavorite: state.filterFavorite,
                        filterSession: false,
                        searchWord: state.searchText
                    )
                )
            }
            return .none
        case .showDayFilterSheet:
            state.showDayFilterSheet = true
            return .none
        case .showCategoryFilterSheet:
            state.showCategoryFilterSheet = true
            return .none
        case .hideDayFilterSheet:
            state.showDayFilterSheet = false
            return .none
        case .hideCategoryFilterSheet:
            state.showCategoryFilterSheet = false
            state.searchResult = state.dayToTimetable.mapValues { timetable in
                timetable.filtered(
                    filters: Filters(
                        days: state.filterDay.toArray(),
                        categories: state.filterCategories,
                        filterFavorite: state.filterFavorite,
                        filterSession: false,
                        searchWord: state.searchText
                    )
                )
            }
            return .none
        }
    }
)

public struct SearchView: View {
    private let store: Store<SearchState, SearchAction>

    public init(store: Store<SearchState, SearchAction>) {
        self.store = store
    }

    public var body: some View {
        WithViewStore(store) { viewStore in
            NavigationView {
                Group {
                    VStack(alignment: .leading) {
                        SearchFiltersSectionView(store: store)
                            .padding(.top, 16)
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal, 16)
                    if viewStore.searchResult.values.allSatisfy(\.timetableItems.isEmpty) {
                        VStack(alignment: .center) {
                            EmptyResultView()
                        }
                        .frame(maxHeight: .infinity)
                    } else {
                        List {
                            ForEach(viewStore.eventDays) { day in
                                Section(header: Text("\(day)")) {
                                    ForEach(viewStore.searchResult[day]?.contents ?? [], id: \.timetableItem.id.value) { timetableItem in
                                        HStack(alignment: .top, spacing: 33) {
                                            SessionTimeView(
                                                startsAt: timetableItem.timetableItem.startsAt.toDate(),
                                                endsAt: timetableItem.timetableItem.endsAt.toDate()
                                            )
                                            TimetableListItemView(
                                                item: timetableItem.timetableItem,
                                                isFavorite: timetableItem.isFavorited,
                                                onTap: {
                                                    viewStore.send(.selectItem(timetableItem))
                                                },
                                                onFavoriteToggle: { id, isFavorited in
                                                    viewStore.send(.setFavorite(id, isFavorited))
                                                },
                                                searchText: viewStore.searchText
                                            )
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
                await viewStore.send(.fetchCategories).finish()
                await viewStore.send(.refresh).finish()
            }
            .sheet(
                isPresented: viewStore.binding(
                    get: {
                        $0.sessionState != nil
                    },
                    send: .hideSessionSheet
                ),
                onDismiss: {
                    viewStore.send(.hideSessionSheet)
                },
                content: {
                    IfLetStore(
                        store.scope(
                            state: \.sessionState,
                            action: SearchAction.session
                        )
                    ) { sessionStore in
                        SessionView(store: sessionStore)
                    }
                }
            )
            .sheet(
                isPresented: viewStore.binding(
                    get: {
                        $0.showDayFilterSheet
                    },
                    send: .hideDayFilterSheet
                ),
                onDismiss: {
                    viewStore.send(.hideDayFilterSheet)
                },
                content: {
                    DayFilterSheetView(
                        days: viewStore.eventDays,
                        selectedDay: viewStore.filterDay,
                        onClose: {
                            viewStore.send(.hideDayFilterSheet)
                        }, onTap: { day in
                            viewStore.send(.selectDay(day))
                        }
                    )
                    .presentationDetents([.medium, .fraction(0.3)])
                }
            )
            .sheet(
                isPresented: viewStore.binding(
                    get: {
                        $0.showCategoryFilterSheet
                    },
                    send: .hideCategoryFilterSheet
                ),
                onDismiss: {
                    viewStore.send(.hideCategoryFilterSheet)
                },
                content: {
                    CategoryFilterSheetView(
                        categories: viewStore.categories,
                        selectedCategories: viewStore.filterCategories,
                        onDeselect: { category in
                            viewStore.send(.deselectCategory(category))
                        },
                        onSelect: { category in
                            viewStore.send(.selectCategory(category))
                        },
                        onClose: {
                            viewStore.send(.hideCategoryFilterSheet)
                        }
                    )
                }
            )
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
                    sessionsRepository: FakeSessionsRepository(),
                    eventKitClient: EventKitClientMock()
                )
            )
        )
    }
}
#endif
