package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

public data class Staff(
    val id: Int,
    val username: String,
    val profileUrl: String,
    val iconUrl: String
) {
    public companion object
}

public fun Staff.Companion.fakes(): PersistentList<Staff> = persistentListOf(
    Staff(
        id = 1,
        username = "staffA",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://placehold.jp/150x150.png",
    ),
    Staff(
        id = 2,
        username = "staffB",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://placehold.jp/150x150.png",
    ),
    Staff(
        id = 3,
        username = "staffC",
        profileUrl = "https://developer.android.com/",
        iconUrl = "https://placehold.jp/150x150.png",
    )
)
