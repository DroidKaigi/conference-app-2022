package io.github.droidkaigi.confsched2022.modifier

import app.cash.zipline.ZiplineService
import io.github.droidkaigi.confsched2022.model.Timetable

interface TimetableModifier : ZiplineService {
    suspend fun produceModels(
        timetable: Timetable
    ): Timetable
}
