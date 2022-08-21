package io.github.droidkaigi.confsched2022.feature.sessions

import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule

data class SessionsUiModel(
    val scheduleState: ScheduleState,
    val isFilterOn: Boolean
) {
    sealed interface ScheduleState {

        data class Loaded(val schedule: DroidKaigiSchedule) : ScheduleState

        object Loading : ScheduleState
    }
}
