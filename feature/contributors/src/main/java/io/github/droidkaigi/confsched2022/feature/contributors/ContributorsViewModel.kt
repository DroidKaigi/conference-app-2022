package io.github.droidkaigi.confsched2022.feature.contributors

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
import io.github.droidkaigi.confsched2022.model.ContributorsRepository
import io.github.droidkaigi.confsched2022.ui.UiLoadState
import io.github.droidkaigi.confsched2022.ui.asLoadState
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContributorsViewModel @Inject constructor(
    private val contributorsRepository: ContributorsRepository,
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    private val contributorsFlow = contributorsRepository
        .contributors()
        .asLoadState()

    private var appError by mutableStateOf<AppError?>(null)

    val uiModel: State<ContributorsUiModel> = moleculeScope.moleculeComposeState(
        clock = ContextClock
    ) {
        val contributorLoadState by contributorsFlow.collectAsState(initial = UiLoadState.Loading)
        ContributorsUiModel(
            state = contributorLoadState,
            appError = appError
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
                contributorsRepository.refresh()
            } catch (e: AppError) {
                appError = e
            }
        }
    }

    fun onAppErrorNotified() {
        appError = null
    }
}
