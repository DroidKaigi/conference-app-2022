package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
public data class Sponsor(
    val name: String,
    val logo: String,
    val plan: Plan,
    val link: String
) {
    public companion object
}

@Serializable
public enum class Plan {
    PLATINUM,
    GOLD,
    SUPPORTER;

    public companion object {
        public fun ofOrNull(plan: String): Plan? {
            return values().firstOrNull { it.name == plan }
        }
    }

    public val isSupporter: Boolean
        get() = this == SUPPORTER
}

public fun Sponsor.Companion.fakes(): PersistentList<Sponsor> = persistentListOf(
    Sponsor(
        name = "DroidKaigi",
        logo = "https://placehold.jp/150x150.png",
        plan = Plan.SUPPORTER,
        link = "https://developer.android.com/"
    ),
    Sponsor(
        name = "DroidKaigi",
        logo = "https://placehold.jp/150x150.png",
        plan = Plan.SUPPORTER,
        link = "https://developer.android.com/"
    ),
    Sponsor(
        name = "DroidKaigi",
        logo = "https://placehold.jp/150x150.png",
        plan = Plan.SUPPORTER,
        link = "https://developer.android.com/"
    ),
    Sponsor(
        name = "DroidKaigi",
        logo = "https://placehold.jp/150x150.png",
        plan = Plan.SUPPORTER,
        link = "https://developer.android.com/"
    ),
    Sponsor(
        name = "DroidKaigi",
        logo = "https://placehold.jp/150x150.png",
        plan = Plan.SUPPORTER,
        link = "https://developer.android.com/"
    ),
)
