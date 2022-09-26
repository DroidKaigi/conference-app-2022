package io.github.droidkaigi.confsched2022.feature.sponsors

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.sponsorsNavGraph(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    onItemClick: (url: String) -> Unit = { _ -> }
) {
    composable(route = SponsorsNavGraph.sponsorsRoute) {
        SponsorsScreenRoot(
            showNavigationIcon = showNavigationIcon,
            onNavigationIconClick = onNavigationIconClick,
            onItemClick = onItemClick
        )
    }
}

object SponsorsNavGraph {
    const val sponsorsRoute = "sponsors"
}
