package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.persistentListOf

data class Contributor(
    val id: Int,
    val username: String,
    val profileUrl: String?,
    val iconUrl: String,
    val icon32Url: String?,
    val icon64Url: String?,
    val icon128Url: String?
) {
    companion object
}

fun Contributor.Companion.fakes() = persistentListOf(
    Contributor(
        id = 1,
        username = "Pie",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://via.placeholder.com/150",
        icon32Url = null,
        icon64Url = null,
        icon128Url = null
    ),
    Contributor(
        id = 2,
        username = "Oreo",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://via.placeholder.com/150",
        icon32Url = null,
        icon64Url = null,
        icon128Url = null
    )
)
