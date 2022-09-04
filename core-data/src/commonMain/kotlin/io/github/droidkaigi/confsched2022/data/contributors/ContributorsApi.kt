package io.github.droidkaigi.confsched2022.data.contributors

import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.contributors.response.ContributorsResponse
import io.github.droidkaigi.confsched2022.model.Contributor
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

class ContributorsApi(
    private val networkService: NetworkService,
) {
    suspend fun contributors(): PersistentList<Contributor> {
        return networkService.get<ContributorsResponse>(
            url = "https://ssot-api-staging.an.r.appspot.com/events/droidkaigi2022/contributors",
            needAuth = true
        ).toContributorList()
    }
}

fun ContributorsResponse.toContributorList(): PersistentList<Contributor> {
    return contributors.map {
        Contributor(
            id = it.id,
            username = it.username,
            profileUrl = "https://github.com/${it.username}",
            iconUrl = it.iconUrl
        )
    }.toPersistentList()
}
