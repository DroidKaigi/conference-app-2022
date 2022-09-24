package io.github.droidkaigi.confsched2022.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public interface SessionsRepository {
    public fun droidKaigiScheduleFlow(): Flow<DroidKaigiSchedule>

    public fun timetableItemFlow(
        timetableItemId: TimetableItemId
    ): Flow<TimetableItemWithFavorite> {
        return droidKaigiScheduleFlow()
            .map { it.findTimetableItem(timetableItemId) }
    }

    public suspend fun getCategories(): List<TimetableCategory>

    public suspend fun refresh()
    public suspend fun setFavorite(sessionId: TimetableItemId, favorite: Boolean)
}
