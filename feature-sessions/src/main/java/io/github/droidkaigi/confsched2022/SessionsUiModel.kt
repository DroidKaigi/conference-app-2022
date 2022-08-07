package io.github.droidkaigi.confsched2022

import androidx.compose.runtime.Immutable
import io.github.droidkaigi.confsched2022.model.Timetable
@Immutable
data class SessionsUiModel(
    val sessionsState: SessionsState,
    val isFilterOn: Boolean
) {
    sealed interface SessionsState {
        data class Loaded(val sessions: Timetable) : SessionsState

        object Loading : SessionsState
    }
}
