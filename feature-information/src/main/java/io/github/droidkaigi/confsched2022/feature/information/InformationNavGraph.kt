package io.github.droidkaigi.confsched2022.feature.information

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.informationGraph(onNavigationIconClick: () -> Unit) {
    composable(route = InformationNavGraph.informationRoute) {
        InformationScreenRoot(onNavigationIconClick)
    }
}

object InformationNavGraph {
    const val informationRoute = "information"
}
