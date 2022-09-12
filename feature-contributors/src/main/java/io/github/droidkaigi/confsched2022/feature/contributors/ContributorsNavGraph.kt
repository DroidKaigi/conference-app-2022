package io.github.droidkaigi.confsched2022.feature.contributors

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.contributorsNavGraph(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    onLinkClick: (url: String, packageName: String?) -> Unit,
) {
    composable(route = ContributorsNavGraph.contributorsRoute) {
        ContributorsScreenRoot(
            showNavigationIcon = showNavigationIcon,
            onNavigationIconClick = onNavigationIconClick,
            onLinkClick = onLinkClick,
        )
    }
}

object ContributorsNavGraph {
    const val contributorsRoute = "contributors"
}
