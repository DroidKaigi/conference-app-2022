package io.github.droidkaigi.confsched2022.feature.about

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.aboutNavGraph(onNavigationIconClick: () -> Unit) {
    composable(route = AboutNavGraph.aboutRoute) {
        AboutScreenRoot(onNavigationIconClick = onNavigationIconClick)
    }
}

object AboutNavGraph {
    const val aboutRoute = "about"
}
