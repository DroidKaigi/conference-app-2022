package io.github.droidkaigi.confsched2022.designsystem.theme

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

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

@SuppressLint("NewApi")
@Composable
public fun KaigiTheme(
    // Currently, we are not supporting light theme
//    darkTheme: Boolean = isSystemInDarkTheme(),
    isDynamicColorEnabled: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDynamicColorEnabled) {
        dynamicDarkColorScheme(LocalContext.current)
    } else {
        DarkColorPalette
    }

    MaterialTheme(
        colorScheme = colorScheme, typography = Typography, shapes = Shapes, content = content
    )
}
