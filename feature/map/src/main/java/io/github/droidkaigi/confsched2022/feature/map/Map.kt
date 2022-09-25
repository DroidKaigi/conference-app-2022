package io.github.droidkaigi.confsched2022.feature.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.strings.Strings

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
            val minScale = 1f
            val maxScale = 3f
            var scale by remember { mutableStateOf(minScale) }
            var offset by remember { mutableStateOf(Offset.Zero) }
            val state = rememberTransformableState { zoomChange, offsetChange, _ ->
                scale *= zoomChange
                offset += offsetChange
            }
            Image(
                painter = painterResource(id = R.drawable.map),
                contentDescription = "Floor map",
                modifier = Modifier
                    .transformable(state = state)
                    .graphicsLayer(
                        scaleX = scale.coerceIn(minScale, maxScale),
                        scaleY = scale.coerceIn(minScale, maxScale),
                        translationX = offset.x,
                        translationY = offset.y,
                    )
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
    }
}
