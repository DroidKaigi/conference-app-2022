package io.github.droidkaigi.confsched2022

import kotlinx.coroutines.flow.Flow

interface SessionsRepository {
    fun sessions(): Flow<Timetable>
    suspend fun refresh()
}
