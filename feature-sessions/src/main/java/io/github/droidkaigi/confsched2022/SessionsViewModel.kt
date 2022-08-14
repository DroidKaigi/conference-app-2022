package io.github.droidkaigi.confsched2022

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
import io.github.droidkaigi.confsched2022.model.Filters
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.Timetable
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.modifier.SessionsUiModel
import io.github.droidkaigi.confsched2022.modifier.SessionsUiModel.SessionsState
import io.github.droidkaigi.confsched2022.modifier.SessionsZipline
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@HiltViewModel
class SessionsViewModel @Inject constructor(
    private val sessionsRepository: SessionsRepository,
    sessionsZipline: SessionsZipline
) : ViewModel() {
    private val filter = mutableStateOf<Filters>(Filters())

    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)
    private val timetable = sessionsZipline.timetableModifier(
        coroutineScope = viewModelScope,
        initialTimetable = Timetable(),
        sessionsRepository.timetable()
    )

    val state = moleculeScope.moleculeComposeState(clock = ContextClock) {
        val timetable by timetable.collectAsState(initial = Timetable())
        val sessionState by remember {
            derivedStateOf {
                if (timetable.timetableItems.isEmpty()) {
                    SessionsState.Loading
                } else {
                    SessionsState.Loaded(
                        timetable.filtered(filter.value)
                    )
                }
            }
        }
        SessionsUiModel(sessionState, isFilterOn = filter.value.filterFavorite)
    }

    fun onToggleFilter() {
        filter.value = filter.value.copy(!filter.value.filterFavorite)
    }

    fun onFavoriteToggle(sessionId: TimetableItemId) {
        viewModelScope.launch {
            sessionsRepository.setFavorite(
                sessionId,
                !timetable.value.favorites.contains(sessionId)
            )
        }
    }
}
