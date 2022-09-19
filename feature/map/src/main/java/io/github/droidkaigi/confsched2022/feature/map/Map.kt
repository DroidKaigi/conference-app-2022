package io.github.droidkaigi.confsched2022.feature.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.strings.Strings

// FIXME: This is a temporary image.
private const val MAP_IMAGE_URL = "https://user-images.githubusercontent.com" +
    "/5885032/191023087-d595c963-31b9-45e6-be9c-55565192b76e.png"

@Composable
fun MapScreenRoot(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
) {
    Map(
        showNavigationIcon = showNavigationIcon,
        onNavigationIconClick = onNavigationIconClick,
    )
}

@Composable
fun Map(
    modifier: Modifier = Modifier,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
) {
    KaigiScaffold(
        modifier = modifier,
        topBar = {
            KaigiTopAppBar(
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavigationIconClick,
                title = {
                    Text(
                        text = stringResource(Strings.map_top_app_bar_title),
                    )
                },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Map is temporarily unavailable :)",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
