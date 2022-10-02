package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow

public interface SponsorsRepository {
    public fun sponsors(): Flow<PersistentList<Sponsor>>

    public suspend fun refresh()
}
