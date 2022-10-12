package io.github.droidkaigi.confsched2022.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.cancellation.CancellationException

public interface SessionsRepository {
    public fun droidKaigiScheduleFlow(): Flow<DroidKaigiSchedule>

    public fun timetableItemFlow(
        timetableItemId: TimetableItemId
    ): Flow<TimetableItemWithFavorite> {
        return droidKaigiScheduleFlow()
            .map { it.findTimetableItem(timetableItemId) }
    }

    public suspend fun getCategories(): List<TimetableCategory>

    @Throws(AppError::class, CancellationException::class)
    public suspend fun refresh()
    public suspend fun setFavorite(sessionId: TimetableItemId, favorite: Boolean)
}
