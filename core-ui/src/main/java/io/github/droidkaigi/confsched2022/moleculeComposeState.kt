package io.github.droidkaigi.confsched2022

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import kotlinx.coroutines.CoroutineScope

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
