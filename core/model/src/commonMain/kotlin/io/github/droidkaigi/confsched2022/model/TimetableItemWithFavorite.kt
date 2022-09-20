package io.github.droidkaigi.confsched2022.model

import io.github.droidkaigi.confsched2022.model.TimetableItem.Session

public data class TimetableItemWithFavorite(
    val timetableItem: TimetableItem,
    val isFavorited: Boolean
) {
    public companion object
}

public fun TimetableItemWithFavorite.Companion.fake(): TimetableItemWithFavorite {
    return TimetableItemWithFavorite(Session.fake(), true)
}
