package io.github.droidkaigi.confsched2022.feature.map

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.mapGraph() {
    composable(route = MapNavGraph.mapRoute) {
        MapScreenRoot()
    }
}

object MapNavGraph {
    const val mapRoute = "map"
}
