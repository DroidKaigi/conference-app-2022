package io.github.droidkaigi.confsched2022.feature.sponsors

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.feature.sponsors.SponsorItem.Divider
import io.github.droidkaigi.confsched2022.feature.sponsors.SponsorItem.Title
import io.github.droidkaigi.confsched2022.feature.sponsors.SponsorPlan.Gold
import io.github.droidkaigi.confsched2022.feature.sponsors.SponsorPlan.Platinum
import io.github.droidkaigi.confsched2022.feature.sponsors.SponsorPlan.Supporter
import io.github.droidkaigi.confsched2022.model.Plan
import io.github.droidkaigi.confsched2022.model.Plan.GOLD
import io.github.droidkaigi.confsched2022.model.Plan.PLATINUM
import io.github.droidkaigi.confsched2022.model.Plan.SUPPORTER
import io.github.droidkaigi.confsched2022.model.Sponsor
import io.github.droidkaigi.confsched2022.model.SponsorsRepository
import io.github.droidkaigi.confsched2022.ui.UiLoadState
import io.github.droidkaigi.confsched2022.ui.asLoadState
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SponsorsViewModel @Inject constructor(
    sponsorsRepository: SponsorsRepository
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    val uiModel: State<SponsorsUiModel>

    init {
        val sponsorsFlow = sponsorsRepository.sponsors()
            .map { it.mapToSponsorItems() }
            .asLoadState()

        uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
            val sponsors by sponsorsFlow.collectAsState(initial = UiLoadState.Loading)
            SponsorsUiModel(sponsors)
        }
    }

    private fun PersistentList<Sponsor>.mapToSponsorItems(): PersistentList<SponsorItem> {
        val groupedSponsors = groupBy { it.plan }
        val mutableSponsorItemList = mutableListOf<SponsorItem>()
        Plan.values().map { plan ->
            val sponsorPlan = plan.mapToSponsorPlan()
            mutableSponsorItemList.add(Title(sponsorPlan))

            val sponsorList = groupedSponsors
                .getOrDefault(plan, emptyList())
                .map { it.mapTopUiState() }
                .toPersistentList()
            mutableSponsorItemList.add(
                SponsorItem.Sponsors(
                    plan = sponsorPlan,
                    sponsorList = sponsorList
                )
            )

            if (!plan.isSupporter) mutableSponsorItemList.add(Divider)
        }
        return mutableSponsorItemList.toPersistentList()
    }

    private fun Plan.mapToSponsorPlan(): SponsorPlan {
        return when (this) {
            PLATINUM -> Platinum
            GOLD -> Gold
            SUPPORTER -> Supporter
        }
    }

    private fun Sponsor.mapTopUiState(): SponsorUiModel {
        return SponsorUiModel(
            name = name,
            logo = logo,
            plan = plan.mapToSponsorPlan(),
            link = link
        )
    }
}
