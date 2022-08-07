package io.github.droidkaigi.confsched2022

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.droidkaigi.confsched2022.designsystem.theme.DroidKaigiTheme

@Composable
fun DroidKaigiApp(
    calculateWindowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController()
) {
    DroidKaigiTheme {
        NavHost(
            navController = navController,
            startDestination = "sessions"
        ) {
            composable(route = "sessions") {
                Sessions()
            }
        }
    }
}
