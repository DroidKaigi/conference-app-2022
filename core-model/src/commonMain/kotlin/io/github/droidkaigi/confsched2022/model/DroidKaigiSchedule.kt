@file:UseSerializers(
    PersistentMapSerializer::class,
)

package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class DroidKaigiSchedule(
    val dayToTimetable: PersistentMap<DroidKaigi2022Day, Timetable>
) {
    val days = DroidKaigi2022Day.values()
    fun filtered(value: Filters): DroidKaigiSchedule {
        return DroidKaigiSchedule(
            dayToTimetable = dayToTimetable
                .mapValues { it.value.filtered(value) }
                .toPersistentMap()
        )
    }

    companion object {
        fun of(timetable: Timetable): DroidKaigiSchedule {
            return DroidKaigiSchedule(
                DroidKaigi2022Day.values().associateWith { day ->
                    timetable.dayTimetable(day)
                }.toPersistentMap()
            )
        }
    }
}

private fun DroidKaigiSchedule.Companion.fake(): DroidKaigiSchedule {
    return of(Timetable.fake())
}
