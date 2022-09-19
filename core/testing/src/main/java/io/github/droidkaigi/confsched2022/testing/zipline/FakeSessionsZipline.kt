package io.github.droidkaigi.confsched2022.testing.zipline

import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.zipline.SessionsZipline
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

public class FakeSessionsZipline : SessionsZipline {
    override fun timetableModifier(): Flow<suspend (DroidKaigiSchedule) -> DroidKaigiSchedule> {
        // no modification
        return flowOf { it }
    }
}
