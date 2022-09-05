package io.github.droidkaigi.confsched2022.feature.sessions

import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched2022.model.TimetableItemWithFavorite
import io.github.droidkaigi.confsched2022.ui.Result

data class SessionDetailUiModel(
    val sessionDetailState: SessionDetailState,
) {
    sealed interface SessionDetailState {

        data class Loaded(
            val timetableItemWithFavorite: TimetableItemWithFavorite
        ) : SessionDetailState

        object Loading : SessionDetailState

        companion object {
            fun of(timetableItemResult: Result<TimetableItemWithFavorite>): SessionDetailState {
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
