package io.github.droidkaigi.confsched2022.modifier

import io.github.droidkaigi.confsched2022.model.Timetable
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class JsTimetableModifier() : TimetableModifier {
    override suspend fun produceModels(timetable: Timetable): Timetable {
        console.log(timetable)
        return timetable.copy(
            timetableItems = timetable.timetableItems.copy(
                timetable.timetableItems.timetableItems.mapIndexed { index, session ->
                    if (index == 0) {
                        session
                    } else {
                        session
                    }
                }.toImmutableList()
            )
        )
    }
}
