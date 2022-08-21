package io.github.droidkaigi.confsched2022.modifier

import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.MultiLangText
import io.github.droidkaigi.confsched2022.model.TimetableItem.Session
import io.github.droidkaigi.confsched2022.model.TimetableItemList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class JsTimetableModifier() : TimetableModifier {
    override suspend fun produceModels(schedule: DroidKaigiSchedule): DroidKaigiSchedule {
        Logger.d("Hello JS world!")
        return schedule.copy(
            dayToTimetable = schedule.dayToTimetable.mapValues { timetable ->
                timetable.value.copy(
                    timetableItems = TimetableItemList(
                        timetable.value.timetableItems.map {
                            if (it is Session) {
                                it.copy(
                                    message = MultiLangText(
                                        "これはJSからのメッセージ",
                                        "this is js message"
                                    )
                                )
                            } else {
                                it
                            }
                        }.toPersistentList()
                    )
                )
            }.toPersistentMap()
        )
    }
}
