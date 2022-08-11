package io.github.droidkaigi.confsched2022.modifier

import io.github.droidkaigi.confsched2022.model.Timetable

class AndroidTimetableModifier: TimetableModifier {
    override suspend fun produceModels(timetable: Timetable): Timetable {
        return timetable
    }
}