package io.github.droidkaigi.confsched2022.data.contributors

import io.github.droidkaigi.confsched2022.data.ExcludedGitHubUserNames
import io.github.droidkaigi.confsched2022.model.Contributor
import io.github.droidkaigi.confsched2022.model.ContributorsRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

// TODO: Move to core-testing, once contributors server is created
class DataContributorsRepository(
    private val contributorsApi: ContributorsApi,
) : ContributorsRepository {
    override fun contributors(): Flow<PersistentList<Contributor>> {
        return callbackFlow {
            send(
                contributorsApi
                    .contributors()
                    .filterNot { ExcludedGitHubUserNames.SET.contains(it.username) }
                    .toPersistentList()
            )
            awaitClose { }
        }
    }

    override suspend fun refresh() {
        TODO("Not yet implemented")
    }
}
