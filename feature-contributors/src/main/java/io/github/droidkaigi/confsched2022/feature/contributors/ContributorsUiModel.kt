package io.github.droidkaigi.confsched2022.feature.contributors

import io.github.droidkaigi.confsched2022.model.Contributor
import io.github.droidkaigi.confsched2022.ui.Result
import kotlinx.collections.immutable.PersistentList

data class ContributorsUiModel(
    val contributorsState: ContributorsState,
) {
    sealed interface ContributorsState {

        data class Loaded(val contributors: PersistentList<Contributor>) : ContributorsState

        object Loading : ContributorsState

        companion object {
            fun of(scheduleResult: Result<PersistentList<Contributor>>): ContributorsState {
                return when (scheduleResult) {
                    Result.Loading -> {
                        Loading
                    }
                    is Result.Success -> {
                        Loaded(scheduleResult.data)
                    }
                    else -> {
                        // TODO
                        // SessionsState.Error
                        Loading
                    }
                }
            }
        }
    }
}
