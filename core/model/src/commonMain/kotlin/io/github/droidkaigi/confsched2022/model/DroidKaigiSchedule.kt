@file:UseSerializers(
    PersistentMapSerializer::class,
)

package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
@Immutable
public data class DroidKaigiSchedule(
    val dayToTimetable: PersistentMap<DroidKaigi2022Day, Timetable>,
    private val timetable: Timetable
) {
    val days: Array<DroidKaigi2022Day> = DroidKaigi2022Day.values()
    public fun filtered(value: Filters): DroidKaigiSchedule {
        return DroidKaigiSchedule(
            dayToTimetable = dayToTimetable
                .mapValues { it.value.filtered(value) }
                .toPersistentMap(),
            timetable = timetable
        )
    }

    public fun findTimetableItem(id: TimetableItemId): TimetableItemWithFavorite {
        return timetable.contents.first { id == it.timetableItem.id }
    }

    public companion object {
        public fun of(timetable: Timetable): DroidKaigiSchedule {
            return DroidKaigiSchedule(
                DroidKaigi2022Day.values().associateWith { day ->
                    timetable.dayTimetable(day)
                }.toPersistentMap(),
                timetable
            )
        }
    }
}

public fun DroidKaigiSchedule.Companion.fake(): DroidKaigiSchedule {
    return of(Timetable.fake())
}
