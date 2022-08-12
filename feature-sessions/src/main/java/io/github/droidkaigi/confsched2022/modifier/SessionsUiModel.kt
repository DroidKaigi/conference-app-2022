package io.github.droidkaigi.confsched2022.modifier

import io.github.droidkaigi.confsched2022.model.Timetable

data class SessionsUiModel(
    val sessionsState: SessionsState,
    val isFilterOn: Boolean
) {
    sealed interface SessionsState {

        data class Loaded(val sessions: Timetable) : SessionsState

        object Loading : SessionsState
    }
}
