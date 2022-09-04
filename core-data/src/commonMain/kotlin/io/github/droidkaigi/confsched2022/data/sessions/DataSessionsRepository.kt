package io.github.droidkaigi.confsched2022.data.sessions

import io.github.droidkaigi.confsched2022.data.SettingsDatastore
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.Timetable
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DataSessionsRepository(
    val sessionsApi: SessionsApi,
    private val favoriteSessionsDataStore: SettingsDatastore
) : SessionsRepository {
    private val timetableFlow = MutableStateFlow(Timetable())
    override fun droidKaigiScheduleFlow(): Flow<DroidKaigiSchedule> = callbackFlow {
        launch { refresh() }
        combine(
            timetableFlow,
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
        timetableFlow.value = sessionsApi.timetable()
    }

    override suspend fun setFavorite(sessionId: TimetableItemId, favorite: Boolean) {
        if (favorite) {
            favoriteSessionsDataStore.addFavorite(sessionId.value)
        } else {
            favoriteSessionsDataStore.removeFavorite(sessionId.value)
        }
    }
}
