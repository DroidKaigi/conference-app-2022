package io.github.droidkaigi.confsched2022.data.contributors

import io.github.droidkaigi.confsched2022.model.Contributor
import io.github.droidkaigi.confsched2022.model.ContributorsRepository
import io.github.droidkaigi.confsched2022.model.fakes
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

// TODO: Move to core-testing, once contributors server is created
public class FakeContributorsRepository : ContributorsRepository {
    override fun contributors(): Flow<PersistentList<Contributor>> {
        return flowOf(Contributor.fakes())
    }

    override suspend fun refresh() {
        TODO("Not yet implemented")
    }
}
