package io.github.droidkaigi.confsched2022.data.sponsors

import io.github.droidkaigi.confsched2022.model.Sponsor
import io.github.droidkaigi.confsched2022.model.SponsorsRepository
import io.github.droidkaigi.confsched2022.model.fakes
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

public class FakeSponsorsRepository : SponsorsRepository {
    override fun sponsors(): Flow<PersistentList<Sponsor>> = flowOf(Sponsor.fakes())

    override suspend fun refresh() {
    }
}
