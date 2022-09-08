package io.github.droidkaigi.confsched2022.feature.contributors

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.contributorsNavGraph(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
) {
    composable(route = ContributorsNavGraph.contributorsRoute) {
        ContributorsScreenRoot(
            showNavigationIcon = showNavigationIcon,
            onNavigationIconClick = onNavigationIconClick,
        )
    }
}

object ContributorsNavGraph {
    const val contributorsRoute = "contributors"
}
