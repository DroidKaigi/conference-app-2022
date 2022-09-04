package io.github.droidkaigi.confsched2022.data.sessions

import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched2022.data.SettingsDatastore
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DataSessionsRepository(
    val sessionsApi: SessionsApi,
    private val favoriteSessionsDataStore: SettingsDatastore
) : SessionsRepository {
    override fun droidKaigiScheduleFlow(): Flow<DroidKaigiSchedule> = callbackFlow {
        val timetable = sessionsApi.timetable()
        favoriteSessionsDataStore.favoriteSessionIds().collect { favoriteSessionIds ->
            val favorites = favoriteSessionIds.map { TimetableItemId(it) }.toPersistentSet()
            trySend(
                DroidKaigiSchedule.of(timetable.copy(favorites = favorites))
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

    override fun timetableItemFlow(timetableItemId: TimetableItemId): Flow<TimetableItem> =
        callbackFlow {
            // TODO return item with favorites like droidKaigiScheduleFlow (or use it)
            // and make coding better
            val item = sessionsApi.timetableItem(timetableItemId)
            if (item == null) Logger.d("timetableItem according to the id is  not found")
            else trySend(item)

            awaitClose {
                Logger.d("await close")
            }
        }
}
