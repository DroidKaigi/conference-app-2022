package io.github.droidkaigi.confsched2022.data.sponsors

import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched2022.model.Sponsor
import io.github.droidkaigi.confsched2022.model.SponsorsRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

public class DataSponsorsRepository(
    private val sponsorsApi: SponsorsApi
) : SponsorsRepository {
    override fun sponsors(): Flow<PersistentList<Sponsor>> =
        callbackFlow {
            val sponsors = sponsorsApi.sponsors()
            Logger.d("sponsors repository: $sponsors")
            send(
                sponsors
            )
            awaitClose { }
        }
}
