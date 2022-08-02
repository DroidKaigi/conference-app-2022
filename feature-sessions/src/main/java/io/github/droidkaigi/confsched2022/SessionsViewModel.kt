package io.github.droidkaigi.confsched2022

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock
import app.cash.molecule.RecompositionClock.ContextClock
import app.cash.molecule.launchMolecule
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.SessionsUiModel.SessionsState
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

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

fun <T> CoroutineScope.moleculeComposeState(
    clock: RecompositionClock,
    body: @Composable () -> T,
): State<T> {
    var mutableState: MutableState<T>? = null

    launchMolecule(
        clock,
        emitter = { value ->
            val outputFlow = mutableState
            if (outputFlow != null) {
                outputFlow.value = value
            } else {
                mutableState = mutableStateOf(value)
            }
        },
        body = body,
    )

    return mutableState!!
}
