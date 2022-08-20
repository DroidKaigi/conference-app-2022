@file:UseSerializers(
    PersistentListSerializer::class,
    ImmutableListSerializer::class,
    ImmutableSetSerializer::class
)

package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class TimetableItemList(
    val timetableItems: ImmutableList<TimetableItem> = persistentListOf(),
) : ImmutableList<TimetableItem> by timetableItems {
    fun getDayTimetableItems(day: DroidKaigi2022Day): TimetableItemList {
        return TimetableItemList(
            timetableItems.filter {
                it.startsAt in day.start..day.end
            }.toImmutableList()
        )
    }
}
