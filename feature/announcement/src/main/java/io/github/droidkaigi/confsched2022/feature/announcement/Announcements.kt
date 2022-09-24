package io.github.droidkaigi.confsched2022.feature.announcement

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiColors
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.model.AnnouncementsByDate
import io.github.droidkaigi.confsched2022.model.fakes
import io.github.droidkaigi.confsched2022.strings.Strings
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Error
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Loading
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Success
import kotlinx.collections.immutable.PersistentList
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn

@Composable
fun AnnouncementsScreenRoot(
    onLinkClick: (url: String) -> Unit,
    showNavigationIcon: Boolean = true,
    viewModel: AnnouncementsViewModel = hiltViewModel(),
    onNavigationIconClick: () -> Unit,
) {
    val uiModel by viewModel.uiModel
    Announcements(
        onLinkClick = onLinkClick,
        showNavigationIcon = showNavigationIcon,
        uiModel = uiModel,
        onRetryButtonClick = { viewModel.onRetryButtonClick() },
        onRetryDismissed = { viewModel.onRetryShown() },
        onNavigationIconClick = onNavigationIconClick
    )
}

@Composable
fun Announcements(
    uiModel: AnnouncementsUiModel,
    showNavigationIcon: Boolean,
    onRetryButtonClick: () -> Unit,
    onRetryDismissed: () -> Unit,
    modifier: Modifier = Modifier,
    onLinkClick: (url: String) -> Unit = { _ -> },
    onNavigationIconClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    KaigiScaffold(
        snackbarHostState = snackbarHostState,
        modifier = modifier,
        topBar = {
            KaigiTopAppBar(
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavigationIconClick,
                title = {
                    Text(
                        text = stringResource(Strings.announcement_top_app_bar_title),
                    )
                },
            )
        }
    ) { innerPadding ->
        AppErrorSnackbarEffect(
            appError = uiModel.appError,
            snackBarHostState = snackbarHostState,
            onRetryDismissed = onRetryDismissed,
            onRetryButtonClick = onRetryButtonClick
        )
        when (uiModel.state) {
            is Error -> {
                // Do nothing
            }
            is Success -> {
                if (uiModel.state.value.isNotEmpty()) {
                    AnnouncementContentList(
                        announcements = uiModel.state.value,
                        innerPadding = innerPadding,
                        onLinkClick = onLinkClick,
                    )
                } else {
                    EmptyBody()
                }
            }
            Loading -> {
                FullScreenLoading()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnnouncementContentList(
    announcements: PersistentList<AnnouncementsByDate>,
    innerPadding: PaddingValues,
    onLinkClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier.padding(innerPadding)
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            announcements.forEach {
                stickyHeader {
                    AnnouncementsHeader(
                        dayString = it.publishedAt.convertString()
                    )
                }
                itemsIndexed(it.announcements) { index, announcement ->
                    AnnouncementContent(
                        type = AnnouncementType.valueOf(announcement.type),
                        title = announcement.title,
                        content = announcement.content,
                        onLinkClick = onLinkClick,
                    )
                    if (index >= announcements.lastIndex) {
                        Divider(
                            modifier = modifier
                                .padding(start = 16.dp, end = 16.dp),
                            thickness = 1.dp,
                            color = Color(KaigiColors.neutralVariantKeyColor60)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnnouncementsHeader(
    dayString: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = dayString,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 32.dp, bottom = 24.dp),
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun AnnouncementContent(
    type: AnnouncementType,
    title: String,
    content: String,
    onLinkClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val regex = "(https)(:\\/\\/[\\w\\/:%#\\\$&\\?\\(\\)~\\.=\\+\\-]+)".toRegex()

    Column(
        modifier = Modifier.clickable {
            regex.find(content)?.let {
                onLinkClick(it.value)
            }
        },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = type.iconRes),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(type.titleTextColor),
            )
        }
        Spacer(
            modifier = modifier
                .height(16.dp)
                .width(26.dp)
        )
        Text(
            modifier = modifier
                .padding(start = 26.dp, top = 8.dp, bottom = 24.dp),
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(type.contentTextColor),
        )
    }
}

@Composable
fun EmptyBody(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            style = MaterialTheme.typography.bodyLarge,
            text = stringResource(Strings.announcement_content_empty)
        )
    }
}

@Composable
fun FullScreenLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun LocalDate.convertString(): String {
    val systemTZ = TimeZone.currentSystemDefault()
    val today = Clock.System.todayIn(systemTZ)
    val yesterday = today.minus(1, DateTimeUnit.DAY)

    return if (this.compareTo(today) == 0) {
        stringResource(Strings.announcement_content_header_title_today)
    } else if (this.compareTo(yesterday) == 0) {
        stringResource(Strings.announcement_content_header_title_yesterday)
    } else {
        "${this.year}/${this.month.value.toString().padStart(2, '0')}/${this.dayOfMonth}"
    }
}

@Preview(showBackground = true)
@Composable
fun AnnouncementsPreview() {
    KaigiTheme {
        Announcements(
            uiModel = AnnouncementsUiModel(
                state = Success(
                    AnnouncementsByDate.fakes()
                ),
                appError = null
            ),
            showNavigationIcon = true,
            onRetryButtonClick = {},
            onRetryDismissed = {},
        ) {}
    }
}
