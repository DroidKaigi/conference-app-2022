package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import co.touchlab.kermit.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.model.Filters
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.ui.UiLoadState
import io.github.droidkaigi.confsched2022.ui.asLoadState
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import io.github.droidkaigi.confsched2022.zipline.SessionsZipline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionsViewModel @Inject constructor(
    private val sessionsRepository: SessionsRepository,
    sessionsZipline: SessionsZipline
) : ViewModel() {

    private val filters = mutableStateOf(Filters())
    private val isTimetableMode = mutableStateOf(true)

    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    val uiModel: State<SessionsUiModel>

    init {
        val ziplineScheduleModifierFlow = sessionsZipline.timetableModifier()
        val sessionScheduleFlow = sessionsRepository.droidKaigiScheduleFlow()

        val scheduleFlow = combine(
            ziplineScheduleModifierFlow,
            sessionScheduleFlow,
            ::Pair
        ).map { (modifier, schedule) ->
            try {
                modifier(schedule)
            } catch (e: Exception) {
                Logger.d(throwable = e) { "Zipline modifier error" }
                schedule
            }
        }.asLoadState()

        uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
            val schedule by scheduleFlow.collectAsState(initial = UiLoadState.Loading)

            SessionsUiModel(
                state = schedule,
                isFilterOn = filters.value.filterFavorite,
                isTimetable = isTimetableMode.value,
            )
        }
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

    fun onTimetableModeToggle(currentIsTimetableMode: Boolean) {
        viewModelScope.launch {
            isTimetableMode.value = !currentIsTimetableMode
        }
    }
}
