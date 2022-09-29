package io.github.droidkaigi.confsched2022.feature.setting

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.settingNavGraph(
    appUiModel: AppUiModel,
    showNavigationIcon: Boolean,
    onDynamicColorToggle: (Boolean) -> Unit,
    onNavigationIconClick: () -> Unit
) {
    composable(route = SettingNavGraph.settingRoute) {
        SettingScreenRoot(
            appUiModel = appUiModel,
            onDynamicColorToggle = onDynamicColorToggle,
            showNavigationIcon = showNavigationIcon,
            onNavigationIconClick = onNavigationIconClick
        )
    }
}

object SettingNavGraph {
    const val settingRoute = "setting"
}
