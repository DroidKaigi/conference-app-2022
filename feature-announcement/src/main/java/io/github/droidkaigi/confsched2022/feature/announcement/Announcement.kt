package io.github.droidkaigi.confsched2022.feature.announcement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.feature.announcement.R.string

@Composable
fun AnnouncementScreenRoot(
    showNavigationIcon: Boolean = true,
    onNavigationIconClick: () -> Unit,
) {
    Announcement(showNavigationIcon, onNavigationIconClick)
}

@Composable
fun Announcement(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
) {
    KaigiScaffold(
        topBar = {
            KaigiTopAppBar(
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavigationIconClick,
                title = {
                    Text(
                        text = stringResource(id = string.announcement_top_app_bar_title),
                    )
                },
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Announcement's screen is unavailable.",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
