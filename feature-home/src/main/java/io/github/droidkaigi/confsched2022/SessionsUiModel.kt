package io.github.droidkaigi.confsched2022

class SessionsUiModel(
    val sessionListState: SessionListState
) {
    sealed interface SessionListState {
        class Loaded() : SessionListState
        object Loading : SessionListState
    }
}