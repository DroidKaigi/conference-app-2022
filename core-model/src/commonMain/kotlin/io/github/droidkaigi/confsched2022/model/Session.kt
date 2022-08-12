package io.github.droidkaigi.confsched2022.model

import kotlinx.serialization.Serializable
@Serializable
data class Session(
    val id: String,
    val title: String = "title"
)
