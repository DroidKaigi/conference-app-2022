package io.github.droidkaigi.confsched2022.feature.staff

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
import io.github.droidkaigi.confsched2022.model.AppError
import io.github.droidkaigi.confsched2022.model.StaffRepository
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Loading
import io.github.droidkaigi.confsched2022.ui.asLoadState
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaffViewModel @Inject constructor(
    private val staffRepository: StaffRepository,
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    private val staffFlow = staffRepository
        .staff()
        .asLoadState()

    private var appError by mutableStateOf<AppError?>(null)

    val uiModel: State<StaffUiModel> = moleculeScope.moleculeComposeState(clock = ContextClock) {
        val staffState by staffFlow.collectAsState(initial = Loading)
        StaffUiModel(
            state = staffState,
            appError = appError,
        )
    }

    init {
        refresh()
    }

    fun onRetryButtonClick() {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            try {
                staffRepository.refresh()
            } catch (e: AppError) {
                appError = e
            }
        }
    }

    fun onAppErrorNotified() {
        appError = null
    }
}
