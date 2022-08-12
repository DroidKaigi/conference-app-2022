package io.github.droidkaigi.confsched2022.data.sessions

import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.Timetable
import kotlinx.coroutines.flow.Flow

class FakeSessionsRepository : SessionsRepository {
    override fun timetable(): Flow<Timetable> {
        TODO("Not yet implemented")
    }

    override suspend fun refresh() {
        TODO("Not yet implemented")
    }

    override suspend fun setFavorite(sessionId: String, favorite: Boolean) {
        TODO("Not yet implemented")
    }
}
