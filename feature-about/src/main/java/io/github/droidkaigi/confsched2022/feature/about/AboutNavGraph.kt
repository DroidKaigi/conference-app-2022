package io.github.droidkaigi.confsched2022.feature.about

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.aboutNavGraph(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
) {
    composable(route = AboutNavGraph.aboutRoute) {
        AboutScreenRoot(
            showNavigationIcon = showNavigationIcon,
            onNavigationIconClick = onNavigationIconClick,
        )
    }
}

object AboutNavGraph {
    const val aboutRoute = "about"
}
