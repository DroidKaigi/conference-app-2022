package io.github.droidkaigi.confsched2022.data.sessions

import io.github.droidkaigi.confsched2022.model.Session
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.Timetable
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DataSessionsRepository(val sessionsApi: SessionsApi) : SessionsRepository {
    override fun timetable(): Flow<Timetable> {
        return flowOf(Timetable(persistentListOf(Session("id", "title"))))
    }

    override suspend fun refresh() {
        // TODO
    }
}
