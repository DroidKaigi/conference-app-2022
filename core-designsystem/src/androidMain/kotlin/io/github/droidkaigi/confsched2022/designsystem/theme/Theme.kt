package io.github.droidkaigi.confsched2022.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = Color(KaigiColors.primaryKeyColor80),
    onPrimary = Color(KaigiColors.primaryKeyColor20),
    primaryContainer = Color(KaigiColors.primaryKeyColor30),
    onPrimaryContainer = Color(KaigiColors.primaryKeyColor90),
    secondary = Color(KaigiColors.secondaryKeyColor80),
    onSecondary = Color(KaigiColors.secondaryKeyColor20),
    secondaryContainer = Color(KaigiColors.secondaryKeyColor30),
    onSecondaryContainer = Color(KaigiColors.secondaryKeyColor90),
    tertiary = Color(KaigiColors.tertiaryKeyColor80),
    onTertiary = Color(KaigiColors.tertiaryKeyColor20),
    tertiaryContainer = Color(KaigiColors.tertiaryKeyColor30),
    onTertiaryContainer = Color(KaigiColors.tertiaryKeyColor90),
    error = Color(KaigiColors.errorKeyColor80),
    errorContainer = Color(KaigiColors.errorKeyColor30),
    onError = Color(KaigiColors.errorKeyColor20),
    onErrorContainer = Color(KaigiColors.errorKeyColor90),
    background = Color(KaigiColors.neutralKeyColor10),
    onBackground = Color(KaigiColors.neutralKeyColor90),
    surface = Color(KaigiColors.neutralKeyColor10),
    onSurface = Color(KaigiColors.neutralKeyColor90),
    surfaceVariant = Color(KaigiColors.neutralVariantKeyColor30),
    onSurfaceVariant = Color(KaigiColors.neutralVariantKeyColor80),
    outline = Color(KaigiColors.neutralVariantKeyColor60),
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
