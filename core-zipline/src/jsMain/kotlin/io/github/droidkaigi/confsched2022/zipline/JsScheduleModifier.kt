package io.github.droidkaigi.confsched2022.zipline

import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.MultiLangText
import io.github.droidkaigi.confsched2022.model.TimetableItem.Session
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
                    if (timetableItem is Session &&
                        // Day 1 lunch
                        timetableItem.id == TimetableItemId("b8528bb4-284c-424e-8be5-a4c1721e4ba8")
                    ) {
                        timetableItem.copy(
                            message = MultiLangText(
                                enTitle = "This is a js message",
                                jaTitle = "これはJSからのメッセージ",
                            )
                        )
                    } else {
                        timetableItem
                    }
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
