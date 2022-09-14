package io.github.droidkaigi.confsched2022.feature.sessions

import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.Filters
import io.github.droidkaigi.confsched2022.ui.UiLoadState

data class SearchUiModel(
    val state: UiLoadState<DroidKaigiSchedule>
) {
    fun filter(filters: Filters): UiLoadState<DroidKaigiSchedule> =
        state.mapSuccess { it.filtered(filters) }
}
