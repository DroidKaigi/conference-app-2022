package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.cancellation.CancellationException

public interface AnnouncementsRepository {
    public fun announcements(): Flow<PersistentList<AnnouncementsByDate>>
    @Throws(AppError::class, CancellationException::class)
    public suspend fun refresh()
}
