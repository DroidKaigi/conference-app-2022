package io.github.droidkaigi.confsched2022.feature.announcement

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.announcementGraph(
    showNavigationIcon: Boolean,
    onLinkClick: (url: String) -> Unit,
    onNavigationIconClick: () -> Unit,
) {
    composable(route = AnnouncementNavGraph.announcementRoute) {
        AnnouncementsScreenRoot(
            showNavigationIcon = showNavigationIcon,
            onLinkClick = onLinkClick,
            onNavigationIconClick = onNavigationIconClick
        )
    }
}

object AnnouncementNavGraph {
    const val announcementRoute = "announcement"
}
