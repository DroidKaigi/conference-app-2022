package io.github.droidkaigi.confsched2022.feature.staff

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.feature.staff.StaffUiModel.StaffState
import io.github.droidkaigi.confsched2022.model.StaffRepository
import io.github.droidkaigi.confsched2022.ui.Result
import io.github.droidkaigi.confsched2022.ui.asResult
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltViewModel
class StaffViewModel @Inject constructor(
    private val staffRepository: StaffRepository,
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)
    val uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
        val staffResult by remember {
            staffRepository.staff()
                .asResult()
        }.collectAsState(initial = Result.Loading)

        val staffState by remember {
            derivedStateOf {
                StaffState.of(staffResult)
            }
        }
        StaffUiModel(staffState)
    }
}
