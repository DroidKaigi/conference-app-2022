package io.github.droidkaigi.confsched2022

data class SessionsUiModel(
    val sessionsState: SessionsState,
    val isFilterOn: Boolean
) {
    sealed interface SessionsState {
        class Loaded(sessions: Sessions) : SessionsState
        object Loading : SessionsState
    }
}
