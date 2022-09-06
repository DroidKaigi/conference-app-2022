package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.github.droidkaigi.confsched2022.model.TimetableItemId

fun NavGraphBuilder.sessionsNavGraph(
    onNavigationIconClick: () -> Unit,
    onSearchIconClick: () -> Unit,
    onTimetableClick: (TimetableItemId) -> Unit,
) {
    composable(route = SessionsNavGraph.sessionRoute) {
        SessionsScreenRoot(
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
            onNavigationIconClick = onNavigationIconClick,
        )
    }

    composable(
        route = SessionsNavGraph.sessionSearchRoute(),
    ) {
        Search()
    }
}

object SessionsNavGraph {
    const val sessionRoute = "sessions"
    fun sessionDetailRoute(sessionId: String) =
        "session/detail/$sessionId"

    fun sessionSearchRoute() =
        "session/search"
}
