package io.github.droidkaigi.confsched2022.feature.staff

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.staffNavGraph(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit
) {
    composable(route = StaffNavGraph.staffRoute) {
        StaffScreenRoot(
            showNavigationIcon = showNavigationIcon,
            onNavigationIconClick = onNavigationIconClick
        )
    }
}

object StaffNavGraph {
    const val staffRoute = "staff"
}
