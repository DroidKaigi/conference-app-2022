package io.github.droidkaigi.confsched2022.feature.sessions

import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.ui.Result

data class TimeTableDetailUiModel(
    val timetableDetailState: TimetableDetailState,
) {
    sealed interface TimetableDetailState {

        data class Loaded(val timetableItem: TimetableItem) : TimetableDetailState

        object Loading : TimetableDetailState

        companion object {
            fun of(timetableItemResult: Result<TimetableItem>): TimetableDetailState {
                return when (timetableItemResult) {
                    Result.Loading -> {
                        Loading
                    }
                    is Result.Success -> {
                        Loaded(timetableItemResult.data)
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
