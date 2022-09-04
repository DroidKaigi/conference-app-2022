package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.droidkaigi.confsched2022.feature.sessions.TimeTableDetailUiModel.TimetableDetailState
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.ui.Result
import io.github.droidkaigi.confsched2022.ui.asResult
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TimeTableDetailViewModel @AssistedInject constructor(
    @Assisted private val timetableItemId: TimetableItemId,
    private val sessionsRepository: SessionsRepository,
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    val uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
        val timetableItemResult by remember {
            sessionsRepository.timetableItemFlow(timetableItemId).asResult()
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

    @AssistedFactory
    interface Factory {
        fun create(timetableItemId: TimetableItemId): TimeTableDetailViewModel
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            assistedFactory: Factory, // this is the Factory interface declared above
            timetableItemId: TimetableItemId
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(timetableItemId) as T
            }
        }
    }
}
