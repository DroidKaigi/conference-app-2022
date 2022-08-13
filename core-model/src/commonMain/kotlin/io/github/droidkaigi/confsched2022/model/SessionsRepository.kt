package io.github.droidkaigi.confsched2022.model

import kotlinx.coroutines.flow.Flow

interface SessionsRepository {
    fun timetable(): Flow<Timetable>
    suspend fun refresh()
    suspend fun setFavorite(sessionId: TimetableItemId, favorite: Boolean)
}
