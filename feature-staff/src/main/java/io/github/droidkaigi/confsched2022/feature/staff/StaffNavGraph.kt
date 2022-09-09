package io.github.droidkaigi.confsched2022.feature.staff

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.staffNavGraph(onNavigationIconClick: () -> Unit) {
    composable(route = StaffNavGraph.staffRoute) {
        StaffScreenRoot(onNavigationIconClick = onNavigationIconClick)
    }
}

object StaffNavGraph {
    const val staffRoute = "staff"
}
