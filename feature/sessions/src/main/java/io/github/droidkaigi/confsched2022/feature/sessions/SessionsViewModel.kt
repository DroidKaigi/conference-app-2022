package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import co.touchlab.kermit.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.model.AppError
import io.github.droidkaigi.confsched2022.model.Filters
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimeLine
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
    sessionsZipline: SessionsZipline,
    private val sessionAlert: SessionAlarm,
) : ViewModel() {

    private val filters = mutableStateOf(Filters())
    private val isTimetableMode = mutableStateOf(true)
    private val timeLine = mutableStateOf(TimeLine.now())
    private var appError by mutableStateOf<AppError?>(null)

    val uiModel: State<SessionsUiModel> = run {
        val moleculeScope =
            CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

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
        moleculeScope.moleculeComposeState(clock = ContextClock) {
            val schedule by scheduleFlow.collectAsState(initial = UiLoadState.Loading)

            SessionsUiModel(
                state = schedule,
                isFilterOn = filters.value.filterFavorite,
                isTimetable = isTimetableMode.value,
                timeLine = timeLine.value,
                appError = appError,
            )
        }
    }

    init {
        refresh()
    }

    fun onLifecycleResume() {
        timeLine.value = TimeLine.now()
    }

    fun onRetryButtonClick() {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            try {
                sessionsRepository.refresh()
            } catch (error: AppError) {
                appError = error
            }
        }
    }

    fun onAppErrorNotified() {
        appError = null
    }

    fun onFavoriteToggle(session: TimetableItem, currentIsFavorite: Boolean) {
        viewModelScope.launch {
            sessionsRepository.setFavorite(
                session.id, currentIsFavorite.not()
            )
            sessionAlert.toggleRegister(
                session, currentIsFavorite.not()
            )
        }
    }

    fun onTimetableModeToggle(currentIsTimetableMode: Boolean) {
        viewModelScope.launch {
            isTimetableMode.value = !currentIsTimetableMode
        }
    }
}
