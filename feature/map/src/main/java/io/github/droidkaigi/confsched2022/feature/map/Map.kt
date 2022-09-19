package io.github.droidkaigi.confsched2022.feature.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.feature.map.R.drawable
import io.github.droidkaigi.confsched2022.strings.Strings

// FIXME: This is a temporary image.
private const val MAP_IMAGE_URL = "https://user-images.githubusercontent.com" +
    "/5885032/191023087-d595c963-31b9-45e6-be9c-55565192b76e.png"

@Composable
fun MapScreenRoot(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit = {},
    onPinClick: () -> Unit = { Logger.w("TODO: Pin is not implemented yet!") },
) {
    Map(
        showNavigationIcon = showNavigationIcon,
        onNavigationIconClick = onNavigationIconClick,
        onPinClick = onPinClick,
    )
}

@Composable
fun Map(
    modifier: Modifier = Modifier,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    onPinClick: () -> Unit,
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
        bottomBar = {
            MapBottomAppBar(
                onPinClick = onPinClick,
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
            Text(
                text = "Map is temporarily unavailable :)",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun MapBottomAppBar(
    onPinClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BottomAppBar(modifier = modifier) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { onPinClick() }) {
                    Icon(
                        painter = painterResource(id = drawable.ic_pin),
                        contentDescription = "pin",
                    )
                }
            }
        }
    }
}
