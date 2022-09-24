package io.github.droidkaigi.confsched2022.feature.sponsors

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import io.github.droidkaigi.confsched2022.model.AppError
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SponsorsViewModel @Inject constructor(
    private val sponsorsRepository: SponsorsRepository
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    private val sponsorsFlow = sponsorsRepository
        .sponsors()
        .map { it.mapToSponsorItems() }
        .asLoadState()

    private var appError by mutableStateOf<AppError?>(null)

    val uiModel: State<SponsorsUiModel> = moleculeScope.moleculeComposeState(
        clock = ContextClock
    ) {
        val sponsorLoadState by sponsorsFlow.collectAsState(initial = UiLoadState.Loading)
        SponsorsUiModel(
            state = sponsorLoadState,
            appError = appError
        )
    }

    init {
        refresh()
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

    fun onRetryButtonClick() {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            try {
                sponsorsRepository.refresh()
            } catch (e: AppError) {
                appError = e
            }
        }
    }

    fun onAppErrorNotified() {
        appError = null
    }
}
