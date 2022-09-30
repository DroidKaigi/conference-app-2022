import appioscombined
import ComposableArchitecture
import Event
import SwiftUI

struct SearchFiltersSectionView: View {
    private let store: Store<SearchState, SearchAction>
    init(store: Store<SearchState, SearchAction>) {
        self.store = store
    }
    var body: some View {
        WithViewStore(store) { viewStore in
            ScrollView(.horizontal) {
                HStack(spacing: 8) {
                    FilterButtonView(
                        title: viewStore.filterDay?.name ?? StringsKt.shared.search_filter_select_day.localized(),
                        isFiltered: viewStore.filterDay != nil
                    ) {
                        viewStore.send(.showDayFilterSheet)
                    }
                    FilterButtonView(
                        title: viewStore.filterCategories.isEmpty ? StringsKt.shared.search_filter_select_category
                            .localized(): viewStore.filterCategories.map { $0.title.currentLangTitle }.joined(separator: ","),
                        isFiltered: !viewStore.filterCategories.isEmpty
                    ) {
                        viewStore.send(.showCategoryFilterSheet)
                    }
                    FavoriteToggleButtonView(isFavorite: viewStore.filterFavorite) {
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
