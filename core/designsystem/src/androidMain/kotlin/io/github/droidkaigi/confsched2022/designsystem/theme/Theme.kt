package io.github.droidkaigi.confsched2022.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import io.github.droidkaigi.confsched2022.core.model.R

@Composable
public fun KaigiTheme(
    // Currently, we are not supporting light theme
//    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = darkColorScheme(
        primary = colorResource(id = R.color.primary),
        onPrimary = colorResource(id = R.color.onPrimary),
        primaryContainer = colorResource(id = R.color.primaryContainer),
        onPrimaryContainer = colorResource(id = R.color.onPrimaryContainer),
        secondary = colorResource(id = R.color.secondary),
        onSecondary = colorResource(id = R.color.onSecondary),
        secondaryContainer = colorResource(id = R.color.secondaryContainer),
        onSecondaryContainer = colorResource(id = R.color.onSecondaryContainer),
        tertiary = colorResource(id = R.color.tertiary),
        onTertiary = colorResource(id = R.color.onTertiary),
        tertiaryContainer = colorResource(id = R.color.tertiaryContainer),
        onTertiaryContainer = colorResource(id = R.color.onTertiaryContainer),
        error = colorResource(id = R.color.error),
        onError = colorResource(id = R.color.onError),
        errorContainer = colorResource(id = R.color.errorContainer),
        onErrorContainer = colorResource(id = R.color.onErrorContainer),
        background = colorResource(id = R.color.background),
        onBackground = colorResource(id = R.color.onBackground),
        surface = colorResource(id = R.color.surface),
        onSurface = colorResource(id = R.color.onSurface),
        surfaceVariant = colorResource(id = R.color.surfaceVariant),
        onSurfaceVariant = colorResource(id = R.color.onSurfaceVariant),
    )

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
