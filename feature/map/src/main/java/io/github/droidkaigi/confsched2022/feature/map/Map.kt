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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.strings.Strings

// FIXME: This is a temporary image.
private const val MapImageUrl = "https://user-images.githubusercontent.com" +
    "/5885032/191032572-b128660f-bff2-4cd4-8228-27cc8f8974a9.png"

@Composable
fun MapScreenRoot(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit = {},
) {
    Map(
        showNavigationIcon = showNavigationIcon,
        onNavigationIconClick = onNavigationIconClick,
    )
}

@Composable
fun Map(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
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
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = MapImageUrl,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
                contentDescription = "Floor map",
            )
        }
    }
}
