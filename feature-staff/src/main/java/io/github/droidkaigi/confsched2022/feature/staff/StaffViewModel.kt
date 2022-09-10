package io.github.droidkaigi.confsched2022.feature.staff

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.model.StaffRepository
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Loading
import io.github.droidkaigi.confsched2022.ui.asLoadState
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltViewModel
class StaffViewModel @Inject constructor(
    staffRepository: StaffRepository,
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    val uiModel: State<StaffUiModel>

    init {
        val dataFlow = staffRepository.staff().asLoadState()

        uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
            val data by dataFlow.collectAsState(initial = Loading)
            StaffUiModel(data)
        }
    }
}
