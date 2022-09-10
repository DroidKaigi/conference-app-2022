package io.github.droidkaigi.confsched2022.feature.contributors

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.model.ContributorsRepository
import io.github.droidkaigi.confsched2022.ui.UiLoadState
import io.github.droidkaigi.confsched2022.ui.asLoadState
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltViewModel
class ContributorsViewModel @Inject constructor(
    contributorsRepository: ContributorsRepository,
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    val uiModel: State<ContributorsUiModel>

    init {
        val dataFlow = contributorsRepository.contributors().asLoadState()

        uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
            val data by dataFlow.collectAsState(initial = UiLoadState.Loading)
            ContributorsUiModel(data)
        }
    }
}
