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
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.ui.asResult
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import io.github.droidkaigi.confsched2022.ui.Result
import kotlinx.coroutines.flow.map

@HiltViewModel
class TimeTableDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionsRepository: SessionsRepository,
) : ViewModel() {

    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    private val timetableItemIdFlow: StateFlow<TimetableItemId> =
        savedStateHandle.getStateFlow("timetableItemId", TimetableItemId(""))

    private val timetableItemResult = MutableStateFlow<Result<TimetableItem>>(Result.Loading)

    init {
        moleculeScope.launch {
            timetableItemIdFlow.map { id ->
                sessionsRepository.timetableItemFlow(id).asResult()
                    .map {
                        timetableItemResult.emit(it)
                    }
            }
        }
    }

    val uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
        val resultState = timetableItemResult.collectAsState()

        val timetableDetailState by remember {
            derivedStateOf {
                TimetableDetailState.of(resultState.value)
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
