package io.github.droidkaigi.confsched2022.feature.setting

import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.model.DroidKaigi2022Day
import io.github.droidkaigi.confsched2022.model.DynamicColorSettingRepository
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KaigiAppViewModel @Inject constructor(
    private val dynamicColorSettingRepository: DynamicColorSettingRepository,
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    private val dynamicColorEnabledFlow: Flow<Boolean> =
        dynamicColorSettingRepository.dynamicEnabledFlow()

    val uiModel: State<AppUiModel> = moleculeScope.moleculeComposeState(clock = ContextClock) {
        val dynamicColorSettingEnabled by dynamicColorEnabledFlow.collectAsState(
            initial = DroidKaigi2022Day.defaultDyamicThemeDate()
        )
        AppUiModel(isDynamicColorEnabled = dynamicColorSettingEnabled && isSupportedDynamicColor())
    }

    fun onDynamicColorToggle(isDynamic: Boolean) {
        viewModelScope.launch {
            dynamicColorSettingRepository.setDynamicColorEnabled(isDynamic)
        }
    }

    @ChecksSdkIntAtLeast(api = VERSION_CODES.S)
    private fun isSupportedDynamicColor(): Boolean {
        return Build.VERSION.SDK_INT >= VERSION_CODES.S
    }
}
