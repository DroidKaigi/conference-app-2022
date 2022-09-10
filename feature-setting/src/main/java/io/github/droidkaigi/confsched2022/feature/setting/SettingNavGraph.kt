package io.github.droidkaigi.confsched2022.feature.setting

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.settingNavGraph(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit
) {
    composable(route = SettingNavGraph.settingRoute) {
        SettingScreenRoot(
            showNavigationIcon = showNavigationIcon,
            onNavigationIconClick = onNavigationIconClick
        )
    }
}

object SettingNavGraph {
    const val settingRoute = "setting"
}