package io.github.droidkaigi.confsched2022.data.sessions

import io.github.droidkaigi.confsched2022.data.PreferenceDatastore
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.Timetable
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.fake
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DataSessionsRepository(
    val sessionsApi: SessionsApi,
    val favoriteSessionsDataStore: PreferenceDatastore
) : SessionsRepository {
    override fun droidKaigiScheduleFlow(): Flow<DroidKaigiSchedule> = callbackFlow {
//        val sessions = sessionsApi.timetable()
        favoriteSessionsDataStore.favoriteSessionIds().collect { favoriteSessionIds ->
            val favorites = favoriteSessionIds.map { TimetableItemId(it) }.toImmutableSet()
            trySend(
                DroidKaigiSchedule.of(Timetable.fake().copy(favorites = favorites))
            )
        }
    }

    override suspend fun refresh() {
        // TODO
    }

    override suspend fun setFavorite(sessionId: TimetableItemId, favorite: Boolean) {
        if (favorite) {
            favoriteSessionsDataStore.addFavorite(sessionId.value)
        } else {
            favoriteSessionsDataStore.removeFavorite(sessionId.value)
        }
    }
}
