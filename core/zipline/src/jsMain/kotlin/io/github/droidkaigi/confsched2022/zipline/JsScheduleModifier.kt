package io.github.droidkaigi.confsched2022.zipline

import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.MultiLangText
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItem.Session
import io.github.droidkaigi.confsched2022.model.TimetableItem.Special
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.TimetableItemList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class JsScheduleModifier() : ScheduleModifier {
    override suspend fun modify(schedule: DroidKaigiSchedule): DroidKaigiSchedule {
        Logger.d("Hello JS world!")
        return schedule.copy(
            dayToTimetable = schedule.dayToTimetable.mapValues { timetable ->
                val modifiedSessions = timetable.value.timetableItems.map { timetableItem ->
                    timetableItem.modified()
                }
                timetable.value.copy(
                    timetableItems = TimetableItemList(
                        modifiedSessions.toPersistentList()
                    )
                )
            }.toPersistentMap()
        )
    }
}

private fun TimetableItem.modified(): TimetableItem {
    return if (this is Special &&
        // Day 1 "App bars" Lunch session
        id == TimetableItemId("b8528bb4-284c-424e-8be5-a4c1721e4ba8") &&
        targetAudience == "TBW"
    ) {
        copy(
            targetAudience = "To be written.",
        )
    } else if (
        this is Session &&
        // Day 3 "Codelabs & Pathways"
        id == TimetableItemId("4cc517f7-11be-4681-8f6a-762786f98a63")
    ) {
        this.copy(
            message = MultiLangText(
                enTitle = "Thank you for your participation.",
                jaTitle = "参加いただきありがとうございました。",
            )
        )
    } else {
        this
    }
}
