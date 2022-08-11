package io.github.droidkaigi.confsched2022.modifier

import io.github.droidkaigi.confsched2022.model.Timetable
import kotlinx.serialization.Serializable

@Serializable
data class SessionsUiModel(
    val sessionsState: SessionsState,
    val isFilterOn: Boolean
) {
    @Serializable
    sealed interface SessionsState {

        @Serializable data class Loaded(val sessions: Timetable) : SessionsState


        @Serializable object Loading : SessionsState
    }
}
