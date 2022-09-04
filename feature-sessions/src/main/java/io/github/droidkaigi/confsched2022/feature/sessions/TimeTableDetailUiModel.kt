package io.github.droidkaigi.confsched2022.feature.sessions

import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched2022.model.TimetableItemWithFavorite
import io.github.droidkaigi.confsched2022.ui.Result

data class TimeTableDetailUiModel(
    val timetableDetailState: TimetableDetailState,
) {
    sealed interface TimetableDetailState {

        data class Loaded(
            val timetableItemWithFavorite: TimetableItemWithFavorite
        ) : TimetableDetailState

        object Loading : TimetableDetailState

        companion object {
            fun of(timetableItemResult: Result<TimetableItemWithFavorite>): TimetableDetailState {
                return when (timetableItemResult) {
                    Result.Loading -> {
                        Loading
                    }
                    is Result.Success -> {
                        Loaded(timetableItemResult.data)
                    }
                    is Result.Error -> {
                        timetableItemResult.exception?.let {
                            Logger.d(it) {
                                "timetableItemResult error"
                            }
                        }
                        Loading
                    }
                }
            }
        }
    }
}
