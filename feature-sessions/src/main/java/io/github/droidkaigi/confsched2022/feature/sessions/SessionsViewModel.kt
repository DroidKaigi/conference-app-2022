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
import co.touchlab.kermit.Logger
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
    private val filters = mutableStateOf(Filters())

    private val ziplineScheduleModifierFlow =
        sessionsZipline.timetableModifier(coroutineScope = viewModelScope)
    private val scheduleResultFlow = combine(
        ziplineScheduleModifierFlow,
        sessionsRepository.droidKaigiScheduleFlow(),
        ::Pair
    )
        .map { (modifier, schedule) ->
            try {
                modifier(schedule)
            } catch (e: Exception) {
                Logger.d(throwable = e) { "Zipline modifier error" }
                schedule
            }
        }
        .asResult()

    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)
    val uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
        val scheduleResult by scheduleResultFlow.collectAsState(initial = Result.Loading)

        val scheduleState by remember {
            derivedStateOf {
                val scheduleState = ScheduleState.of(scheduleResult)
                scheduleState.filter(filters.value)
            }
        }
        SessionsUiModel(scheduleState = scheduleState, isFilterOn = filters.value.filterFavorite)
    }

    fun onToggleFilter() {
        filters.value = filters.value.copy(!filters.value.filterFavorite)
    }

    fun onFavoriteToggle(sessionId: TimetableItemId, currentIsFavorite: Boolean) {
        viewModelScope.launch {
            sessionsRepository.setFavorite(
                sessionId, !currentIsFavorite
            )
        }
    }
}
