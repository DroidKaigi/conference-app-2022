package io.github.droidkaigi.confsched2022

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.SessionsUiModel.SessionsState
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltViewModel
class SessionsViewModel @Inject constructor() : ViewModel() {
    val filter = mutableStateOf(false)

    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    val state = moleculeScope.moleculeComposeState(clock = ContextClock) {
        SessionsUiModel(SessionsState.Loading, isFilterOn = filter.value)
    }

    fun onToggle() {
        println("toggle")
        filter.value = !filter.value
    }
}

