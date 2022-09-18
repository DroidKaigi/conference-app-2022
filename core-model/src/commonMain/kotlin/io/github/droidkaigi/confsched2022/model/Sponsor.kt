package io.github.droidkaigi.confsched2022.model

import kotlinx.serialization.Serializable

@Serializable
public data class Sponsor(
    val name: String,
    val logo: String,
    val plan: Plan,
    val link: String
)

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
}
