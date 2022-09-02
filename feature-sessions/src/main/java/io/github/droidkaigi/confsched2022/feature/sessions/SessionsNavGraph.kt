package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.github.droidkaigi.confsched2022.model.TimetableItemId

fun NavGraphBuilder.sessionsNavGraph(
    onNavigationIconClick: () -> Unit,
    onTimetableClick: (TimetableItemId) -> Unit,
) {
    composable(route = SessionsNavGraph.sessionRoute) {
        SessionsScreenRoot(
            onNavigationIconClick = onNavigationIconClick,
            onTimetableClick = onTimetableClick,
        )
    }

    composable(
        route = "${SessionsNavGraph.sessionDetail}{id}",
        arguments = listOf(
            navArgument("id") {
                type = NavType.StringType
            }
        )
    ) {
        // TODO savableにする
        val id = it.arguments?.getString("id") ?: ""
        TimetableDetailScreenRoot(
            timetableItemId = TimetableItemId(id),
            onNavigationIconClick = onNavigationIconClick,
        )
    }
}

object SessionsNavGraph {
    const val sessionRoute = "sessions"
    const val sessionDetail = "session/detail/"
}
