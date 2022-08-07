package io.github.droidkaigi.confsched2022

import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SessionsDataRepository(val sessionsApi: SessionsApi) : SessionsRepository {
    override fun sessions(): Flow<Timetable> {
        return flowOf(Timetable(persistentListOf(Session())))
    }

    override suspend fun refresh() {
        // TODO
    }
}