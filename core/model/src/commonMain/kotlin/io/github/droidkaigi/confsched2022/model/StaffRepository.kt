package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.cancellation.CancellationException

public interface StaffRepository {
    public fun staff(): Flow<PersistentList<Staff>>
    @Throws(AppError::class, CancellationException::class)
    public suspend fun refresh()
}
