package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.github.droidkaigi.confsched2022.model.TimetableCategory
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItemId

fun NavGraphBuilder.sessionsNavGraph(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    onCategoryTagClick: (category: TimetableCategory) -> Unit,
    onLinkClick: (url: String) -> Unit,
    onBackIconClick: () -> Unit,
    onSearchIconClick: () -> Unit,
    onTimetableClick: (TimetableItemId) -> Unit,
    onNavigateFloorMapClick: () -> Unit,
    onShareClick: (TimetableItem) -> Unit,
    onRegisterCalendarClick: (TimetableItem) -> Unit
) {
    composable(route = SessionsNavGraph.sessionRoute) {
        SessionsScreenRoot(
            showNavigationIcon = showNavigationIcon,
            onNavigationIconClick = onNavigationIconClick,
            onSearchClicked = onSearchIconClick,
            onTimetableClick = onTimetableClick,
        )
    }

    composable(
        route = SessionsNavGraph.sessionDetailRoute("{id}"),
        arguments = listOf(
            navArgument("id") {
                type = NavType.StringType
            }
        )
    ) {
        // TODO make it savable
        val id = it.arguments?.getString("id") ?: ""
        SessionDetailScreenRoot(
            timetableItemId = TimetableItemId(id),
            onCategoryTagClick = onCategoryTagClick,
            onLinkClick = onLinkClick,
            onBackIconClick = onBackIconClick,
            onNavigateFloorMapClick = onNavigateFloorMapClick,
            onShareClick = onShareClick,
            onRegisterCalendarClick = onRegisterCalendarClick
        )
    }

    composable(
        route = SessionsNavGraph.sessionSearchRoute("{id}"),
        arguments = listOf(
            navArgument("id") {
                type = NavType.StringType
            }
        )
    ) {
        val id = it.arguments?.getString("id") ?: ""
        SearchRoot(
            categoryId = id,
            onItemClick = onTimetableClick,
            onBackIconClick = onBackIconClick,
        )
    }
}

object SessionsNavGraph {
    const val sessionRoute = "sessions"
    fun sessionDetailRoute(sessionId: String) =
        "session/detail/$sessionId"

    fun sessionSearchRoute(categoryId: String?) =
        "session/search/$categoryId"
}
