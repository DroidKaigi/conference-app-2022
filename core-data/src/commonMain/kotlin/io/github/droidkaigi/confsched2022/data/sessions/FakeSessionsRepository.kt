package io.github.droidkaigi.confsched2022.data.sessions

import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.Timetable
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.fake
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeSessionsRepository : SessionsRepository {
    private val favorites = MutableStateFlow(persistentSetOf<TimetableItemId>())
    private val isTimetableModeFlow = MutableStateFlow(true)
    override fun droidKaigiScheduleFlow(): Flow<DroidKaigiSchedule> {
        val timetable = Timetable.fake()
        return favorites.map {
            DroidKaigiSchedule.of(
                timetable.copy(
                    favorites = it
                )
            )
        }
    }

    override suspend fun refresh() {
    }

    override suspend fun setFavorite(sessionId: TimetableItemId, favorite: Boolean) {
        if (favorite) {
            favorites.value = favorites.value.add(sessionId)
        } else {
            favorites.value = favorites.value.remove(sessionId)
        }
    }

    override fun isTimetableModeFlow(): Flow<Boolean> {
        return isTimetableModeFlow
    }

    override suspend fun setIsTimetableMode(isTimetableMode: Boolean) {
        isTimetableModeFlow.emit(isTimetableMode)
    }

    // for test
    val savedFavorites get() = favorites.value
}
