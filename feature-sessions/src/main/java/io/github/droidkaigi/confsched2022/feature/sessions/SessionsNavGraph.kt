package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.sessionsNavGraph(onNavigationIconClick: () -> Unit) {
    composable(route = SessionsNavGraph.sessionRoute) {
        SessionsScreenRoot(onNavigationIconClick = onNavigationIconClick)
    }
}

object SessionsNavGraph {
    const val sessionRoute = "sessions"
}
