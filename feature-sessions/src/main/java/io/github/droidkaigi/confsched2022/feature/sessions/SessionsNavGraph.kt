package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.sessionsNavGraph() {
    composable(route = SessionsNavGraph.sessionRoute) {
        SessionsScreenRoot()
    }
}

object SessionsNavGraph {
    const val sessionRoute = "sessions"
}
