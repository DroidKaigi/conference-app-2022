package io.github.droidkaigi.confsched2022.feature.announcement

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.model.AnnouncementsRepository
import io.github.droidkaigi.confsched2022.model.AppError
import io.github.droidkaigi.confsched2022.ui.UiLoadState
import io.github.droidkaigi.confsched2022.ui.asLoadState
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementsViewModel @Inject constructor(
    private val announcementsRepository: AnnouncementsRepository,
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    private val announcementsFlow = announcementsRepository
        .announcements()
        .asLoadState()

    private var appError by mutableStateOf<AppError?>(null)

    val uiModel: State<AnnouncementsUiModel> = moleculeScope.moleculeComposeState(
        clock = ContextClock
    ) {
        val announcementLoadState by announcementsFlow.collectAsState(initial = UiLoadState.Loading)
        AnnouncementsUiModel(
            state = announcementLoadState,
            appError = appError
        )
    }

    init {
        refresh()
    }

    fun onRetryButtonClick() {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            try {
                announcementsRepository.refresh()
            } catch (e: AppError) {
                appError = e
            }
        }
    }

    fun onRetryShown() {
        appError = null
    }
}
