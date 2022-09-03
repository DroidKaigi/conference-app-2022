package io.github.droidkaigi.confsched2022.feature.contributors

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.contributorsNavGraph(onNavigationIconClick: () -> Unit) {
    composable(route = ContributorsNavGraph.contributorsRoute) {
        ContributorsScreenRoot(onNavigationIconClick = onNavigationIconClick)
    }
}

object ContributorsNavGraph {
    const val contributorsRoute = "contributors"
}
