package io.github.droidkaigi.confsched2022.data.sponsors

import io.github.droidkaigi.confsched2022.BuildKonfig
import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.sponsors.response.SponsorResponse
import io.github.droidkaigi.confsched2022.model.Plan
import io.github.droidkaigi.confsched2022.model.Sponsor
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

public class SponsorsApi(
    private val networkService: NetworkService
) {
    public suspend fun sponsors(): PersistentList<Sponsor> {
        return networkService.get<List<SponsorResponse>>(
            url = "${BuildKonfig.apiUrl}/sponsors"
        ).map { it.toSponsor() }.toPersistentList()
    }
}

internal fun SponsorResponse.toSponsor(): Sponsor {
    return Sponsor(
        name = sponsorName,
        logo = sponsorLogo,
        plan = Plan.ofOrNull(plan) ?: Plan.SUPPORTER,
        link = link
    )
}
