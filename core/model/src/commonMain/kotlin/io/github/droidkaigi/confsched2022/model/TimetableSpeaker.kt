package io.github.droidkaigi.confsched2022.model

import kotlinx.serialization.Serializable

@Serializable
public data class TimetableSpeaker(
    val id: String,
    val name: String,
    val iconUrl: String,
    val bio: String,
    val tagLine: String,
)
