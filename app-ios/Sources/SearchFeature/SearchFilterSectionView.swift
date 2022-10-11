import appioscombined
import ComposableArchitecture
import Event
import SwiftUI

struct SearchFiltersSectionView: View {
    private let store: Store<SearchState, SearchAction>

    struct ViewState: Equatable {
        public var dayFilterTitle: String
        public var categoryFilterTitle: String
        public var filterFavorite: Bool
        public var filterDays: [DroidKaigi2022Day]
        public var filterCategories: [TimetableCategory]

        public init(state: SearchState) {
            self.dayFilterTitle = state.filterDays.isEmpty ? StringsKt.shared.search_filter_select_day.localized() : state.filterDays.map(\.name).joined(separator: ",")
            self.categoryFilterTitle = state.filterCategories.isEmpty ? StringsKt.shared.search_filter_select_category.localized() : state.filterCategories.map(\.title.currentLangTitle).joined(separator: ",")
            self.filterFavorite = state.filterFavorite
            self.filterDays = state.filterDays
            self.filterCategories = state.filterCategories
        }
    }

    public init(store: Store<SearchState, SearchAction>) {
        self.store = store
    }

    public var body: some View {
        WithViewStore(store.scope(state: ViewState.init)) { viewStore in
            ScrollView(.horizontal) {
                HStack(spacing: 8) {
                    SearchFilterButtonView(
                        type: .select,
                        title: viewStore.dayFilterTitle,
                        isFiltered: !viewStore.filterDays.isEmpty
                    ) {
                        viewStore.send(.showDayFilterSheet)
                    }
                    SearchFilterButtonView(
                        type: .select,
                        title: viewStore.categoryFilterTitle,
                        isFiltered: !viewStore.filterCategories.isEmpty
                    ) {
                        viewStore.send(.showCategoryFilterSheet)
                    }
                    SearchFilterButtonView(
                        type: .toggle,
                        title: StringsKt.shared.search_filter_favorites.localized(),
                        isFiltered: viewStore.filterFavorite
                    ) {
                        viewStore.send(.filterFavorite)
                    }
                }
            }
        }
    }
}

#if DEBUG
struct SearchFiltersSection_Previews: PreviewProvider {
    static var previews: some View {
        SearchFiltersSectionView(
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
