package io.github.droidkaigi.confsched2022.zipline

import app.cash.zipline.ZiplineService
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule

interface TimetableModifier : ZiplineService {
    suspend fun produceModels(
        schedule: DroidKaigiSchedule
    ): DroidKaigiSchedule
}
