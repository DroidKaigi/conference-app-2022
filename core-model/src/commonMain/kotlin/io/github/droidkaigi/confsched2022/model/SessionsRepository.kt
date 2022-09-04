package io.github.droidkaigi.confsched2022.model

import kotlinx.coroutines.flow.Flow

interface SessionsRepository {
    fun droidKaigiScheduleFlow(): Flow<DroidKaigiSchedule>
    suspend fun refresh()
    suspend fun setFavorite(sessionId: TimetableItemId, favorite: Boolean)
    fun timetableItemFlow(timetableItemId: TimetableItemId): Flow<TimetableItem>
}
