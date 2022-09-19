package io.github.droidkaigi.confsched2022.feature.sponsors

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.strings.Strings

@Composable
fun SponsorsScreenRoot(
    modifier: Modifier = Modifier,
    showNavigationIcon: Boolean = true,
    onNavigationIconClick: () -> Unit = {}
) {
    Sponsors(
        modifier = modifier,
        showNavigationIcon = showNavigationIcon,
        onNavigationIconClick = onNavigationIconClick
    )
}

@Composable
fun Sponsors(
    showNavigationIcon: Boolean,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit
) {
    KaigiScaffold(
        topBar = {
            KaigiTopAppBar(
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavigationIconClick,
                title = {
                    Text(
                        text = stringResource(Strings.sponsors_top_app_bar_title),
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Coming Soon...")
        }
    }
}
