package io.github.droidkaigi.confsched2022.feature.sessions

import io.github.droidkaigi.confsched2022.model.AppError
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.Filters
import io.github.droidkaigi.confsched2022.model.TimeLine
import io.github.droidkaigi.confsched2022.ui.UiLoadState

data class SessionsUiModel(
    val state: UiLoadState<DroidKaigiSchedule>,
    val isFilterOn: Boolean,
    val isTimetable: Boolean,
    val timeLine: TimeLine?,
    val appError: AppError?,
) {
    fun filter(filters: Filters): UiLoadState<DroidKaigiSchedule> =
        state.mapSuccess { it.filtered(filters) }
}
