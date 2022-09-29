package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.model.AppError
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.ui.UiLoadState
import io.github.droidkaigi.confsched2022.ui.asLoadState
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionsRepository: SessionsRepository,
) : ViewModel() {

    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    private val timetableItemId: TimetableItemId =
        TimetableItemId(requireNotNull(savedStateHandle.get<String>("id")))

    private var appError by mutableStateOf<AppError?>(null)

    private val timetableItemFlow =
        sessionsRepository.timetableItemFlow(timetableItemId).asLoadState()

    val uiModel: State<SessionDetailUiModel> =
        moleculeScope.moleculeComposeState(clock = ContextClock) {
            val timetableItem by timetableItemFlow.collectAsState(initial = UiLoadState.Loading)
            SessionDetailUiModel(
                state = timetableItem,
                appError = appError
            )
        }

    init {
        refresh()
    }

    fun onFavoriteToggle(sessionId: TimetableItemId, currentIsFavorite: Boolean) {
        viewModelScope.launch {
            sessionsRepository.setFavorite(
                sessionId, !currentIsFavorite
            )
        }
    }

    fun onRetryButtonClick() {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            try {
                sessionsRepository.refresh()
            } catch (e: AppError) {
                appError = e
            }
        }
    }

    fun onAppErrorNotified() {
        appError = null
    }
}
