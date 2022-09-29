package io.github.droidkaigi.confsched2022.data.contributors

import io.github.droidkaigi.confsched2022.model.Contributor
import io.github.droidkaigi.confsched2022.model.ContributorsRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow

// TODO: Move to core-testing, once contributors server is created
public class DataContributorsRepository(
    private val contributorsApi: ContributorsApi,
) : ContributorsRepository {
    private val contributorsStateFlow =
        MutableStateFlow<PersistentList<Contributor>>(persistentListOf())

    override fun contributors(): Flow<PersistentList<Contributor>> {
        return callbackFlow {
            contributorsStateFlow.collect {
                send(it)
            }
        }
    }

    override suspend fun refresh() {
        contributorsStateFlow.value = contributorsApi
            .contributors()
            .toPersistentList()
    }
}
