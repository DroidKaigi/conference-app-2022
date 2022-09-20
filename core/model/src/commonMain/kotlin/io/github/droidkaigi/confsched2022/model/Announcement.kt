package io.github.droidkaigi.confsched2022.model

import kotlinx.serialization.Serializable

@Serializable
public data class Announcement(
    val id: Int,
    val title: String,
    val content: String,
    val type: String,
    val language: String,
) {
    public companion object
}
