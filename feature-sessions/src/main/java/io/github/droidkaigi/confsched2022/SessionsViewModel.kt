package io.github.droidkaigi.confsched2022

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.modifier.SessionsUiModel.SessionsState
import io.github.droidkaigi.confsched2022.modifier.SessionsUiModel.SessionsState.Loading
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.Timetable
import io.github.droidkaigi.confsched2022.modifier.SessionsUiModel
import io.github.droidkaigi.confsched2022.modifier.SessionsZipline
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope

@HiltViewModel
class SessionsViewModel @Inject constructor(
    sessionsRepository: SessionsRepository,
    sessionsZipline :SessionsZipline
) : ViewModel() {
    val filter = mutableStateOf(false)

    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)
    val timetable = sessionsZipline.timetableModifier(
        coroutineScope = viewModelScope,
        initialTimetable = Timetable(persistentListOf()),
        sessionsRepository.timetable()
    )

    val state = moleculeScope.moleculeComposeState(clock = ContextClock) {
        val timetable by
            timetable.collectAsState(initial = Timetable(persistentListOf()))
        val sessionState = if (timetable.sessions.isEmpty()) {
            Loading
        } else {
            SessionsState.Loaded(timetable)
        }
        SessionsUiModel(sessionState, isFilterOn = filter.value)
    }

    fun onToggle() {
        println("toggle")
        filter.value = !filter.value
    }
}
