package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.feature.sessions.SessionDetailUiModel.SessionDetailState
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.ui.Result
import io.github.droidkaigi.confsched2022.ui.asResult
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val sessionsRepository: SessionsRepository,
) : ViewModel() {

    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    private val timetableItemId: TimetableItemId =
        TimetableItemId(requireNotNull(savedStateHandle.get<String>("id")))

    private val timetableItemFlow = sessionsRepository.timetableItemFlow(timetableItemId).asResult()

    val uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
        val timetableItemResult by timetableItemFlow.collectAsState(initial = Result.Loading)

        val sessionDetailState by remember {
            derivedStateOf {
                SessionDetailState.of(timetableItemResult)
            }
        }
        SessionDetailUiModel(sessionDetailState)
    }

    fun onFavoriteToggle(sessionId: TimetableItemId, currentIsFavorite: Boolean) {
        viewModelScope.launch {
            sessionsRepository.setFavorite(
                sessionId, !currentIsFavorite
            )
        }
    }
}
