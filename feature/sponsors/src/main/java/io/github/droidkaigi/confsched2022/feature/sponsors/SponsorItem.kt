package io.github.droidkaigi.confsched2022.feature.sponsors

import dev.icerock.moko.resources.StringResource
import io.github.droidkaigi.confsched2022.strings.Strings
import kotlinx.collections.immutable.PersistentList

sealed interface SponsorItem {
    data class Title(private val plan: SponsorPlan) : SponsorItem {
        val value: StringResource
            get() = plan.value
    }

    data class Sponsors(
        val plan: SponsorPlan,
        val sponsorList: PersistentList<SponsorUiModel>
    ) : SponsorItem

    object Divider : SponsorItem
}

enum class SponsorPlan(val value: StringResource) {
    Platinum(Strings.sponsors_platinum_sponsors_title),
    Gold(Strings.sponsors_gold_sponsors_title),
    Supporter(Strings.sponsors_supporter_sponsors_title);
}

data class SponsorUiModel(
    val name: String,
    val logo: String,
    val plan: SponsorPlan,
    val link: String
)
