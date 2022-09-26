package io.github.droidkaigi.confsched2022.model

public data class Filters(
    val days: List<DroidKaigi2022Day> = emptyList(),
    val categories: List<TimetableCategory> = emptyList(),
    val filterFavorite: Boolean = false,
    val filterSession: Boolean = false,
    val searchWord: String = ""
)
