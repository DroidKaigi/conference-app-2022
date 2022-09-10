package io.github.droidkaigi.confsched2022.feature.about

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.aboutNavGraph(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
<<<<<<< HEAD
    onStaffListClick: () -> Unit
=======
    onLinkClick: (url: String, packageName: String?) -> Unit,
>>>>>>> main
) {
    composable(route = AboutNavGraph.aboutRoute) {
        AboutScreenRoot(
            showNavigationIcon = showNavigationIcon,
            onNavigationIconClick = onNavigationIconClick,
<<<<<<< HEAD
            onStaffListClick = onStaffListClick
=======
            onLinkClick = onLinkClick,
>>>>>>> main
        )
    }
}

object AboutNavGraph {
    const val aboutRoute = "about"
}
