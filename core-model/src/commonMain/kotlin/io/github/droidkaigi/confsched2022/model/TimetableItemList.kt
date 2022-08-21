@file:UseSerializers(
    PersistentListSerializer::class,
)

package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class TimetableItemList(
    val timetableItems: PersistentList<TimetableItem> = persistentListOf(),
) : PersistentList<TimetableItem> by timetableItems {
    fun getDayTimetableItems(day: DroidKaigi2022Day): TimetableItemList {
        return TimetableItemList(
            timetableItems.filter {
                it.startsAt in day.start..day.end
            }.toPersistentList()
        )
    }
}
