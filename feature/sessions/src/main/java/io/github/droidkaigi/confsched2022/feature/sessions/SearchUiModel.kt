package io.github.droidkaigi.confsched2022.feature.sessions

import io.github.droidkaigi.confsched2022.model.DroidKaigi2022Day
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.TimetableCategory
import io.github.droidkaigi.confsched2022.ui.UiLoadState

data class SearchUiModel(
    val filter: SearchFilterUiModel,
    val filterSheetState: SearchFilterSheetState,
    val state: UiLoadState<DroidKaigiSchedule>
)

data class SearchFilterUiModel(
    val selectedCategories: List<TimetableCategory> = emptyList(),
    val selectedDay: DroidKaigi2022Day? = null,
    val isFavoritesOn: Boolean = false,
) {
    val isDaySelected: Boolean
        get() = selectedDay != null

    val selectedCategoriesValue: String
        get() = selectedCategories.joinToString { it.title.currentLangTitle }

    val isCategoriesSelected: Boolean
        get() = selectedCategories.isNotEmpty()
}

sealed interface SearchFilterSheetState {
    data class ShowDayFilter(val days: List<DroidKaigi2022Day>): SearchFilterSheetState
    data class ShowCategoriesFilterSheet(val categories: List<TimetableCategory>): SearchFilterSheetState
    object Hide: SearchFilterSheetState
}
