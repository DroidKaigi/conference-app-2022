package io.github.droidkaigi.confsched2022.feature.announcement

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementsViewModel @Inject constructor(
    announcementsRepository: AnnouncementsRepository,
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    val uiModel: State<AnnouncementsUiModel>

    init {
        val dataFlow = announcementsRepository.announcements().asLoadState()

        uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
            val data by dataFlow.collectAsState(initial = UiLoadState.Loading)
            AnnouncementsUiModel(data)
        }
    }

    fun onRetryButtonClick() {
        viewModelScope.launch {
            // TODO: Re-acquire data.
        }
    }
}
