package io.github.droidkaigi.confsched2022.data.sessions.response

import kotlinx.serialization.Serializable

@Serializable
internal data class SpeakerResponse(
    val profilePicture: String? = null,
    val sessions: List<Int> = emptyList(),
    val tagLine: String? = null,
    val isTopSpeaker: Boolean?,
    val bio: String? = null,
    val fullName: String,
    val id: String,
)
