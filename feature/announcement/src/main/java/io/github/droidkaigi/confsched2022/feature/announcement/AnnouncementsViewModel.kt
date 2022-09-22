package io.github.droidkaigi.confsched2022.feature.announcement

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.model.AnnouncementsRepository
import io.github.droidkaigi.confsched2022.ui.UiLoadState
import io.github.droidkaigi.confsched2022.ui.asLoadState
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltViewModel
class AnnouncementsViewModel @Inject constructor(
    private val announcementsRepository: AnnouncementsRepository,
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    private val _uiModel: MutableState<AnnouncementsUiModel> = mutableStateOf(
        AnnouncementsUiModel(
            state = UiLoadState.Loading,
        )
    )
    val uiModel: State<AnnouncementsUiModel> = _uiModel

    init {
        fetchAnnouncements()
    }

    fun onRetryButtonClick() {
        fetchAnnouncements()
    }

    private fun fetchAnnouncements() {
        val dataFlow = announcementsRepository.announcements().asLoadState()

        moleculeScope.moleculeComposeState(clock = ContextClock) {
            val data by dataFlow.collectAsState(initial = UiLoadState.Loading)
            _uiModel.value = AnnouncementsUiModel(data)
        }
    }
}
