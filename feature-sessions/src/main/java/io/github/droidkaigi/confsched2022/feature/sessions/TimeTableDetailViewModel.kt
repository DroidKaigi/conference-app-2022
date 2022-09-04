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
import io.github.droidkaigi.confsched2022.feature.sessions.TimeTableDetailUiModel.TimetableDetailState
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.ui.Result
import io.github.droidkaigi.confsched2022.ui.asResult
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimeTableDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionsRepository: SessionsRepository,
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    private val timetableItemIdFlow: StateFlow<TimetableItemId> =
        savedStateHandle.getStateFlow("timetableItemId", TimetableItemId(""))

    val uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
        val timetableItemResult by remember {
            sessionsRepository.timetableItemFlow(timetableItemIdFlow.value).asResult()
        }.collectAsState(initial = Result.Loading)

        val timetableDetailState by remember {
            derivedStateOf {
                TimetableDetailState.of(timetableItemResult = timetableItemResult)
            }
        }
        TimeTableDetailUiModel(timetableDetailState)
    }

    fun onFavoriteToggle(sessionId: TimetableItemId, currentIsFavorite: Boolean) {
        viewModelScope.launch {
            sessionsRepository.setFavorite(
                sessionId, !currentIsFavorite
            )
        }
    }
}
