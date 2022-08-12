package io.github.droidkaigi.confsched2022.data.sessions

import PreferenceDatastore
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.Timetable
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DataSessionsRepository(
    val sessionsApi: SessionsApi,
    val favoriteSessionsDataStore: PreferenceDatastore
) : SessionsRepository {
    override fun timetable(): Flow<Timetable> = callbackFlow {
        val sessions = sessionsApi.sessions()
        favoriteSessionsDataStore.favoriteSessionIds().collect { favoriteSessionIds ->
            val favoriteMarkedSessions = sessions.map { session ->
                session.copy(isFavorite = favoriteSessionIds.contains(session.id))
            }
            trySend(Timetable(favoriteMarkedSessions.toImmutableList()))
        }
    }

    override suspend fun refresh() {
        // TODO
    }

    override suspend fun setFavorite(sessionId: String, favorite: Boolean) {
        if (favorite) {
            favoriteSessionsDataStore.addFavorite(sessionId)
        } else {
            favoriteSessionsDataStore.removeFavorite(sessionId)
        }
    }
}
