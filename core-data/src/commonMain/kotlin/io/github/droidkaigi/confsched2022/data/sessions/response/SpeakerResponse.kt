package io.github.droidkaigi.confsched2022.data.sessions.response

import kotlinx.serialization.Serializable

@Serializable
internal data class SpeakerResponse(
    val profilePicture: String? = null,
    val sessions: List<Int> = emptyList(),
    val tagLine: String = "",
    val isTopSpeaker: Boolean?,
    val bio: String = "",
    val fullName: String,
    val id: String,
)
