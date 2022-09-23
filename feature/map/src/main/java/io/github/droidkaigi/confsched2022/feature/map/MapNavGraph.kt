package io.github.droidkaigi.confsched2022.feature.map

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.mapGraph(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
) {
    composable(route = MapNavGraph.mapRoute) {
        MapScreenRoot(
            showNavigationIcon = showNavigationIcon,
            onNavigationIconClick = onNavigationIconClick,
        )
    }
}

object MapNavGraph {
    const val mapRoute = "map"
}
