package io.github.droidkaigi.confsched2022.model

import io.github.droidkaigi.confsched2022.model.TimetableItem.Session

data class TimetableItemWithFavorite(val timetableItem: TimetableItem, val isFavorited: Boolean) {
    companion object
}

fun TimetableItemWithFavorite.Companion.fake(): TimetableItemWithFavorite {
    return TimetableItemWithFavorite(Session.fake(), true)
}
