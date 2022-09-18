package io.github.droidkaigi.confsched2022.feature.sponsors

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.model.SponsorsRepository
import io.github.droidkaigi.confsched2022.ui.UiLoadState
import io.github.droidkaigi.confsched2022.ui.asLoadState
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltViewModel
class SponsorsViewModel @Inject constructor(
    sponsorsRepository: SponsorsRepository
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    val uiModel: State<SponsorsUiModel>

    init {
        val sponsorsFlow = sponsorsRepository.sponsors().asLoadState()
        uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
            val sponsors by sponsorsFlow.collectAsState(initial = UiLoadState.Loading)
            SponsorsUiModel(sponsors)
        }
    }
}
