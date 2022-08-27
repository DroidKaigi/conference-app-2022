package io.github.droidkaigi.confsched2022.feature.contributors

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.contributorsNavGraph() {
    composable(route = ContributorsNavGraph.contributorsRoute) {
        ContributorsScreenRoot()
    }
}

object ContributorsNavGraph {
    const val contributorsRoute = "contributors"
}
