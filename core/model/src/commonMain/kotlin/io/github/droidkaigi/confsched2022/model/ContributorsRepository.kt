package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.cancellation.CancellationException

public interface ContributorsRepository {
    public fun contributors(): Flow<PersistentList<Contributor>>
    @Throws(AppError::class, CancellationException::class)
    public suspend fun refresh()
}
