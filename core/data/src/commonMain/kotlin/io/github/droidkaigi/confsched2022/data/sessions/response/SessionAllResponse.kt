package io.github.droidkaigi.confsched2022.data.sessions.response

import kotlinx.serialization.Serializable

@Serializable
internal data class SessionAllResponse(
    val sessions: List<SessionResponse> = emptyList(),
    val rooms: List<RoomResponse> = emptyList(),
    val speakers: List<SpeakerResponse> = emptyList(),
    val categories: List<CategoryResponse> = emptyList(),
)
