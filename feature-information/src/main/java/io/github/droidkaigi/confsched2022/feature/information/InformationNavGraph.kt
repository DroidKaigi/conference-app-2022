package io.github.droidkaigi.confsched2022.feature.information

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.informationGraph(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
) {
    composable(route = InformationNavGraph.informationRoute) {
        InformationScreenRoot(
            showNavigationIcon = showNavigationIcon,
            onNavigationIconClick = onNavigationIconClick
        )
    }
}

object InformationNavGraph {
    const val informationRoute = "information"
}
