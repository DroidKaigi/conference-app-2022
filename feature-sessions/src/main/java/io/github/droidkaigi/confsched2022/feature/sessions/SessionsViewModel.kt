package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.feature.sessions.SessionsUiModel.ScheduleState
import io.github.droidkaigi.confsched2022.model.Filters
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.ui.Result
import io.github.droidkaigi.confsched2022.ui.asResult
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import io.github.droidkaigi.confsched2022.zipline.SessionsZipline
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class SessionsViewModel @Inject constructor(
    private val sessionsRepository: SessionsRepository,
    sessionsZipline: SessionsZipline
) : ViewModel() {
    private val filter = mutableStateOf(Filters())

    private val ziplineTimetableModifierFlow =
        sessionsZipline.timetableModifier(coroutineScope = viewModelScope)
    private val timetableResultFlow = combine(
        ziplineTimetableModifierFlow, sessionsRepository.droidKaigiScheduleFlow(), ::Pair
    ).map { (modifier, schedule) -> modifier(schedule) }.asResult()

    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)
    val uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
        val timetableResult by timetableResultFlow.collectAsState(initial = Result.Loading)

        val sessionState by remember {
            derivedStateOf {
                when (val result = timetableResult) {
                    Result.Loading -> {
                        ScheduleState.Loading
                    }
                    is Result.Success -> {
                        ScheduleState.Loaded(result.data.filtered(filter.value))
                    }
                    else -> {
                        // TODO
                        // SessionsState.Error
                        ScheduleState.Loading
                    }
                }
            }
        }
        SessionsUiModel(sessionState, isFilterOn = filter.value.filterFavorite)
    }

    fun onToggleFilter() {
        filter.value = filter.value.copy(!filter.value.filterFavorite)
    }

    fun onFavoriteToggle(sessionId: TimetableItemId, currentIsFavorite: Boolean) {
        viewModelScope.launch {
            sessionsRepository.setFavorite(
                sessionId, !currentIsFavorite
            )
        }
    }
}
