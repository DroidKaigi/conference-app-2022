package io.github.droidkaigi.confsched2022.feature.setting

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.model.Filters
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    val uiModel: State<SettingUiModel>
    private val isDynamicColorEnabled: MutableState<Boolean>

    init {
        // TODO: initialize isDynamicColorEnabled
        isDynamicColorEnabled = mutableStateOf(true)
        uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
            SettingUiModel(isDynamicColorEnabled = isDynamicColorEnabled.value)
        }
    }

    fun onDynamicColorToggle() {
        // TODO: change
        isDynamicColorEnabled.value = !isDynamicColorEnabled.value
    }
}