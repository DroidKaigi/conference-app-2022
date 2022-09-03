package io.github.droidkaigi.confsched2022.data.contributors.response

import kotlinx.serialization.Serializable

@Serializable
data class ContributorsResponse(
    val status: String,
    val contributors: List<ContributorResponse>,
)

@Serializable
data class ContributorResponse(
    val id: Int,
    val username: String,
    val iconUrl: String,
)
