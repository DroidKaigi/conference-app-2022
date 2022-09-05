package io.github.droidkaigi.confsched2022.feature.contributors

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.feature.contributors.ContributorsUiModel.ContributorsState
import io.github.droidkaigi.confsched2022.model.ContributorsRepository
import io.github.droidkaigi.confsched2022.ui.Result
import io.github.droidkaigi.confsched2022.ui.asResult
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltViewModel
class ContributorsViewModel @Inject constructor(
    private val contributorsRepository: ContributorsRepository,
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)
    val uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
        val contributorsResult by remember {
            contributorsRepository.contributors()
                .asResult()
        }.collectAsState(initial = Result.Loading)

        val contributorsState by remember {
            derivedStateOf {
                ContributorsState.of(contributorsResult)
            }
        }
        ContributorsUiModel(contributorsState)
    }
}
