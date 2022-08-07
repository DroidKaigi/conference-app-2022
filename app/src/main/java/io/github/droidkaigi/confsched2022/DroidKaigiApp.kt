package io.github.droidkaigi.confsched2022

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched2022.designsystem.theme.DroidKaigiTheme

@Composable
fun DroidKaigiApp(calculateWindowSizeClass: WindowSizeClass) {
    DroidKaigiTheme {
        Sessions()
    }
}