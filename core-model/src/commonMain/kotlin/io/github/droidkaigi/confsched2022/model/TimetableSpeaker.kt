package io.github.droidkaigi.confsched2022.model

import kotlinx.serialization.Serializable

@Serializable
data class TimetableSpeaker(
    val name: String,
    val iconUrl: String,
    val bio: String,
    val tagLine: String,
)
