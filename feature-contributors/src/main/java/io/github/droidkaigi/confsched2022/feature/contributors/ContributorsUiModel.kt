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
            fun of(contributorResult: Result<PersistentList<Contributor>>): ContributorsState {
                return when (contributorResult) {
                    Result.Loading -> {
                        Loading
                    }
                    is Result.Success -> {
                        Loaded(contributorResult.data)
                    }
                    is Result.Error -> {
                        contributorResult.exception?.printStackTrace()
                        // TODO
                        // SessionsState.Error
                        Loading
                    }
                }
            }
        }
    }
}
