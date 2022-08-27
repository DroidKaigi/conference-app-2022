package io.github.droidkaigi.confsched2022.feature.sessions

import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.Filters
import io.github.droidkaigi.confsched2022.ui.Result

data class SessionsUiModel(
    val scheduleState: ScheduleState,
    val isFilterOn: Boolean
) {
    sealed interface ScheduleState {
        fun filter(filters: Filters): ScheduleState {
            if (this is Loaded) {
                return Loaded(schedule.filtered(filters))
            }
            return this
        }

        data class Loaded(val schedule: DroidKaigiSchedule) : ScheduleState

        object Loading : ScheduleState

        companion object {
            fun of(scheduleResult: Result<DroidKaigiSchedule>): ScheduleState {
                return when (scheduleResult) {
                    Result.Loading -> {
                        Loading
                    }
                    is Result.Success -> {
                        Loaded(scheduleResult.data)
                    }
                    else -> {
                        // TODO
                        // SessionsState.Error
                        Loading
                    }
                }
            }
        }
    }
}
