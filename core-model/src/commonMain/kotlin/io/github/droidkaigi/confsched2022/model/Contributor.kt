package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

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

fun List<Contributor>.fixedSort(): PersistentList<Contributor> {
    val regex = Regex("^[a-zA-Z].*")
    val sortedList = this.sortedWith(
        compareBy<Contributor> {
            if (it.username.matches(regex)) {
                0
            } else {
                1
            }
        }.thenBy {
            it.username
        }
    )

    return sortedList.toPersistentList()
}

fun Contributor.Companion.fakes(): List<Contributor> = listOf(
    Contributor(
        id = 1,
        username = "Pie",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://placehold.jp/150x150.png",
        icon32Url = null,
        icon64Url = null,
        icon128Url = null
    ),
    Contributor(
        id = 2,
        username = "Oreo",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://placehold.jp/150x150.png",
        icon32Url = "https://via.placeholder.com/32",
        icon64Url = "https://via.placeholder.com/64",
        icon128Url = "https://via.placeholder.com/128"
    ),
    Contributor(
        id = 3,
        username = "Android",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://avatars.githubusercontent.com/u/45986582?v=4",
        icon32Url = "https://via.placeholder.com/32",
        icon64Url = "https://via.placeholder.com/64",
        icon128Url = "https://via.placeholder.com/128"
    ),
    Contributor(
        id = 4,
        username = "0x00",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://placehold.jp/150x150.png",
        icon32Url = "https://via.placeholder.com/32",
        icon64Url = "https://via.placeholder.com/64",
        icon128Url = "https://via.placeholder.com/128"
    ),
    Contributor(
        id = 5,
        username = "Cupcake",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://placehold.jp/150x150.png",
        icon32Url = "https://via.placeholder.com/32",
        icon64Url = "https://via.placeholder.com/64",
        icon128Url = "https://via.placeholder.com/128"
    ),
    Contributor(
        id = 6,
        username = "-100ten",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://placehold.jp/150x150.png",
        icon32Url = "https://via.placeholder.com/32",
        icon64Url = "https://via.placeholder.com/64",
        icon128Url = "https://via.placeholder.com/128"
    ),
    Contributor(
        id = 7,
        username = "Anpanman2",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://placehold.jp/150x150.png",
        icon32Url = "https://via.placeholder.com/32",
        icon64Url = "https://via.placeholder.com/64",
        icon128Url = "https://via.placeholder.com/128"
    ),
)

fun Contributor.Companion.fakes() = fakeApiResponse().fixedSort()
