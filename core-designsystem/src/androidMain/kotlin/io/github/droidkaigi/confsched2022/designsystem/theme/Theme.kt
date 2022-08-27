package io.github.droidkaigi.confsched2022.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = Color(KeyColors.primary),
    secondary = Color(KeyColors.secondary),
    tertiary = Color(KeyColors.tertiary),
    error = Color(KeyColors.error),
)

@Composable
fun KaigiTheme(
    // Currently, we are not supporting light theme
//    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
