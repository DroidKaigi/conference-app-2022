package io.github.droidkaigi.confsched2022.data.sponsors

import io.github.droidkaigi.confsched2022.BuildKonfig
import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.sponsors.response.SponsorResponse
import io.github.droidkaigi.confsched2022.data.sponsors.response.SponsorsResponse
import io.github.droidkaigi.confsched2022.model.Plan
import io.github.droidkaigi.confsched2022.model.Sponsor
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

public class SponsorsApi(
    private val networkService: NetworkService
) {
    public suspend fun sponsors(): PersistentList<Sponsor> {
        return networkService.get<SponsorsResponse>(
            url = "${BuildKonfig.apiUrl}/events/droidkaigi2022/sponsor"
        ).toSponsorList()
    }
}

private fun SponsorsResponse.toSponsorList(): PersistentList<Sponsor> {
    return sponsor.map { it.toSponsor() }.toPersistentList()
}

private fun SponsorResponse.toSponsor(): Sponsor {
    return Sponsor(
        name = sponsorName,
        logo = sponsorLogo,
        plan = Plan.ofOrNull(plan) ?: Plan.SUPPORTER,
        link = link
    )
}
