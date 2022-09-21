package io.github.droidkaigi.confsched2022.data.sessions

import io.github.droidkaigi.confsched2022.data.SettingsDatastore
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.TimetableCategory
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

public class DataSessionsRepository(
    private val sessionsApi: SessionsApi,
    private val sessionsDao: SessionsDao,
    private val settingsDatastore: SettingsDatastore
) : SessionsRepository {
    override fun droidKaigiScheduleFlow(): Flow<DroidKaigiSchedule> = callbackFlow {
        launch { refresh() }
        combine(
            sessionsDao.selectAll(),
            settingsDatastore.favoriteSessionIds(),
            ::Pair
        )
            .collect { (timetable, favoriteSessionIds) ->
                val favorites = favoriteSessionIds.map { TimetableItemId(it) }.toPersistentSet()
                trySend(
                    DroidKaigiSchedule.of(timetable.copy(favorites = favorites))
                )
            }
    }

    override suspend fun getCategories(): List<TimetableCategory> {
        return sessionsDao.selectAllCategories()
    }

    override suspend fun refresh() {
        val timetable = sessionsApi.timetable()
        sessionsDao.deleteAll()
        sessionsDao.insert(timetable)
    }

    override suspend fun setFavorite(sessionId: TimetableItemId, favorite: Boolean) {
        if (favorite) {
            settingsDatastore.addFavorite(sessionId.value)
        } else {
            settingsDatastore.removeFavorite(sessionId.value)
        }
    }
}
