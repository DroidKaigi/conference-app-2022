package io.github.droidkaigi.confsched2022.model

public data class Filters(
    val day: DroidKaigi2022Day? = null,
    val categories: List<TimetableCategory> = emptyList(),
    val filterFavorite: Boolean = false,
    val filterSession: Boolean = false,
    val searchWord: String = ""
)
