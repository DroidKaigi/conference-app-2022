package io.github.droidkaigi.confsched2022.data.contributors

import io.github.droidkaigi.confsched2022.BuildKonfig
import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.contributors.response.ContributorsResponse
import io.github.droidkaigi.confsched2022.model.Contributor
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

public class ContributorsApi(
    private val networkService: NetworkService,
) {
    public suspend fun contributors(): PersistentList<Contributor> {
        return networkService.get<ContributorsResponse>(
            url = "${BuildKonfig.apiUrl}/events/droidkaigi2022/contributors"
        ).toContributorList()
    }
}

private fun ContributorsResponse.toContributorList(): PersistentList<Contributor> {
    return contributors.map {
        Contributor(
            id = it.id,
            username = it.username,
            profileUrl = "https://github.com/${it.username}",
            iconUrl = it.iconUrl
        )
    }.toPersistentList()
}
