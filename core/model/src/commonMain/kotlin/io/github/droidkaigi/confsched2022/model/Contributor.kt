package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class Contributor(
    val id: Int,
    val username: String,
    val profileUrl: String?,
    val iconUrl: String,
) {
    companion object
}

fun Contributor.Companion.fakes(): PersistentList<Contributor> = persistentListOf(
    Contributor(
        id = 1,
        username = "Pie",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://placehold.jp/150x150.png",
    ),
    Contributor(
        id = 2,
        username = "Oreo",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://placehold.jp/150x150.png",
    ),
    Contributor(
        id = 3,
        username = "Android",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://avatars.githubusercontent.com/u/45986582?v=4"
    ),
    Contributor(
        id = 4,
        username = "0x00",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://placehold.jp/150x150.png"
    ),
    Contributor(
        id = 5,
        username = "Cupcake",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://placehold.jp/150x150.png"
    ),
    Contributor(
        id = 6,
        username = "-100ten",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://placehold.jp/150x150.png"
    ),
    Contributor(
        id = 7,
        username = "Anpanman2",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://placehold.jp/150x150.png"
    )
)
