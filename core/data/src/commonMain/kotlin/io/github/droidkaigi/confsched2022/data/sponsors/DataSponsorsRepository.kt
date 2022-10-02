package io.github.droidkaigi.confsched2022.data.sponsors

import io.github.droidkaigi.confsched2022.model.Sponsor
import io.github.droidkaigi.confsched2022.model.SponsorsRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow

public class DataSponsorsRepository(
    private val sponsorsApi: SponsorsApi
) : SponsorsRepository {
    private val sponsorsStateFlow =
        MutableStateFlow<PersistentList<Sponsor>>(persistentListOf())
    override fun sponsors(): Flow<PersistentList<Sponsor>> =
        callbackFlow {
            sponsorsStateFlow.collect {
                send(it)
            }
        }

    override suspend fun refresh() {
        sponsorsStateFlow.value = sponsorsApi
            .sponsors()
    }
}
