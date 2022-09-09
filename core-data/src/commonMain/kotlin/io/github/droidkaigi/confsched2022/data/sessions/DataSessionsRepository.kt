package io.github.droidkaigi.confsched2022.data.sessions

import io.github.droidkaigi.confsched2022.data.SettingsDatastore
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DataSessionsRepository(
    val sessionsApi: SessionsApi,
    private val sessionsDao: SessionsDao,
    private val favoriteSessionsDataStore: SettingsDatastore
) : SessionsRepository {
    override fun droidKaigiScheduleFlow(): Flow<DroidKaigiSchedule> = callbackFlow {
        launch { refresh() }
        combine(
            sessionsDao.selectAll(),
            favoriteSessionsDataStore.favoriteSessionIds(),
            ::Pair
        )
            .collect { (timetable, favoriteSessionIds) ->
                val favorites = favoriteSessionIds.map { TimetableItemId(it) }.toPersistentSet()
                trySend(
                    DroidKaigiSchedule.of(timetable.copy(favorites = favorites))
                )
            }
    }

    override suspend fun refresh() {
        val timetable = sessionsApi.timetable()
        sessionsDao.deleteAll()
        sessionsDao.insert(timetable)
    }

    override suspend fun setFavorite(sessionId: TimetableItemId, favorite: Boolean) {
        if (favorite) {
            favoriteSessionsDataStore.addFavorite(sessionId.value)
        } else {
            favoriteSessionsDataStore.removeFavorite(sessionId.value)
        }
    }

    override fun isTimetableModeFlow(): Flow<Boolean> {
        return favoriteSessionsDataStore.isTimetableMode()
    }
    override suspend fun setIsTimetableMode(isTimetableMode: Boolean) {
        favoriteSessionsDataStore.setIsTimetableMode(isTimetableMode)
    }
}
