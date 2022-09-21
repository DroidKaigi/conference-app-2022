package io.github.droidkaigi.confsched2022.feature.announcement

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.announcementGraph(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
) {
    composable(route = AnnouncementNavGraph.announcementRoute) {
        AnnouncementsScreenRoot(
            showNavigationIcon = showNavigationIcon,
            onNavigationIconClick = onNavigationIconClick
        )
    }
}

object AnnouncementNavGraph {
    const val announcementRoute = "announcement"
}
