package io.github.droidkaigi.confsched2022.model

data class Filters(
    val filterFavorite: Boolean = false,
    val filterSession: Boolean = false,
    val searchWord: String = ""
)
