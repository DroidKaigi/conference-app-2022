package io.github.droidkaigi.confsched2022.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SessionsRepository {
    fun droidKaigiScheduleFlow(): Flow<DroidKaigiSchedule>

    fun timetableItemFlow(timetableItemId: TimetableItemId): Flow<TimetableItemWithFavorite> {
        return droidKaigiScheduleFlow()
            .map { it.findTimetableItem(timetableItemId) }
    }

    suspend fun refresh()
    suspend fun setFavorite(sessionId: TimetableItemId, favorite: Boolean)

    fun isTimetableModeFlow(): Flow<Boolean>
    suspend fun setIsTimetableMode(isTimetableMode: Boolean)
}
