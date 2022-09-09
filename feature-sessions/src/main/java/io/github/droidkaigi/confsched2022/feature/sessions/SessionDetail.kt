package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode.Expand
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTag
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.designsystem.theme.TimetableItemColor.AppBar
import io.github.droidkaigi.confsched2022.feature.sessions.SessionDetailUiModel.SessionDetailState.Loaded
import io.github.droidkaigi.confsched2022.model.TimetableAsset
import io.github.droidkaigi.confsched2022.model.TimetableCategory
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItem.Session
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.TimetableItemWithFavorite
import io.github.droidkaigi.confsched2022.model.TimetableRoom
import io.github.droidkaigi.confsched2022.model.TimetableSpeaker
import io.github.droidkaigi.confsched2022.model.fake
import io.github.droidkaigi.confsched2022.ui.LocalCalendarRegistration
import io.github.droidkaigi.confsched2022.ui.LocalShareManager
import kotlinx.collections.immutable.PersistentList
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun SessionDetailScreenRoot(
    modifier: Modifier = Modifier,
    timetableItemId: TimetableItemId,
    onBackIconClick: () -> Unit = {},
    onNavigateFloorMapClick: () -> Unit,
) {

    val viewModel = hiltViewModel<SessionDetailViewModel>()
    val uiModel by viewModel.uiModel

    val shareManager = LocalShareManager.current
    val calendarRegistration = LocalCalendarRegistration.current

    SessionDetailScreen(
        uiModel = uiModel,
        onBackIconClick = onBackIconClick,
        onFavoriteClick = { currentFavorite ->
            viewModel.onFavoriteToggle(timetableItemId, currentFavorite)
        },
        onShareClick = { shareManager.share(it.title.currentLangTitle) },
        onNavigateFloorMapClick = onNavigateFloorMapClick,
        onRegisterCalendarClick = {
            calendarRegistration.register(
                startsAtMilliSeconds = it.startsAt.toEpochMilliseconds(),
                endsAtMilliSeconds = it.endsAt.toEpochMilliseconds(),
                title = it.title.currentLangTitle,
                location = it.room.name.currentLangTitle,
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionDetailTopAppBar(
    onBackIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackIconClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "close"
                )
            }
        },
        title = {},
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme
                .colorScheme
                .surfaceColorAtElevation(elevation)
        )
    )
}

@Composable
fun SessionDetailScreen(
    modifier: Modifier = Modifier,
    uiModel: SessionDetailUiModel,
    onBackIconClick: () -> Unit = {},
    onFavoriteClick: (Boolean) -> Unit = {},
    onShareClick: (TimetableItem) -> Unit = {},
    onNavigateFloorMapClick: () -> Unit = {},
    onRegisterCalendarClick: (TimetableItem) -> Unit = {},
) {
    if (uiModel.sessionDetailState !is Loaded) {
        CircularProgressIndicator()
        return
    }
    val (item, isFavorited) = uiModel.sessionDetailState.timetableItemWithFavorite
    KaigiScaffold(
        topBar = {
            SessionDetailTopAppBar(
                onBackIconClick = onBackIconClick,
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = { onShareClick(item) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = "share",
                            )
                        }
                        IconButton(onClick = onNavigateFloorMapClick) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_02),
                                contentDescription = "go to floor map",
                            )
                        }
                        IconButton(onClick = { onRegisterCalendarClick(item) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_today),
                                contentDescription = "register calendar",
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1F))
                    FloatingActionButton(
                        onClick = {
                            onFavoriteClick(isFavorited)
                        }
                    ) {
                        if (isFavorited) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bookmark_filled),
                                contentDescription = "favorited"
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bookmark),
                                contentDescription = "not favorited"
                            )
                        }
                    }
                }
            }
        }
    ) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            SessionDetailSessionInfo(
                title = item.title.currentLangTitle,
                startsAt = item.startsAt,
                endsAt = item.endsAt,
                room = item.room,
                category = item.category,
                language = item.language,
                levels = item.levels,
            )

            if (item is Session)
                SessionDetailDescription(
                    description = item.description
                )

            SessionDetailTargetAudience(
                targetAudience = item.targetAudience
            )

            if (item is Session)
                SessionDetailSpeakers(
                    speakers = item.speakers,
                )
            SessionDetailAssets(
                asset = item.asset
            )
        }
    }
}

@Composable
fun SessionTagsLine(
    startsAt: Instant,
    endsAt: Instant,
    room: TimetableRoom,
    category: TimetableCategory,
    levels: PersistentList<String>,
) {
    val sessionMinutes = "${(endsAt - startsAt).toComponents { minutes, _, _ -> minutes }}"
    FlowRow(
        mainAxisSize = Expand,
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp
    ) {
        KaigiTag(
            backgroundColor = Color(AppBar.color)
        ) {
            Text(room.name.currentLangTitle)
        }
        KaigiTag(
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Text(sessionMinutes + "min")
        }
        KaigiTag(
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Text(category.title.currentLangTitle)
        }
        levels.forEach {
            KaigiTag(
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text(text = it)
            }
        }
    }
}

@Composable
fun SessionScheduleInfo(
    modifier: Modifier = Modifier,
    startTime: Instant,
    endTime: Instant
) {
    val sessionStartDateTime = startTime
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val sessionEndDateTime = endTime
        .toLocalDateTime(TimeZone.currentSystemDefault())

    fun LocalDateTime.toTime() = "$hour:${minute.toString().padStart(2, '0')}"

    val sessionSchedule =
        "${sessionStartDateTime.monthNumber}月 ${sessionStartDateTime.dayOfMonth}日 " +
            "${sessionStartDateTime.toTime()}-${sessionEndDateTime.toTime()}"

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painterResource(id = R.drawable.ic_schedule),
            contentDescription = "Schedule-Icon",
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = sessionSchedule,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
fun SessionDetailSessionInfo(
    modifier: Modifier = Modifier,
    title: String,
    startsAt: Instant,
    endsAt: Instant,
    room: TimetableRoom,
    category: TimetableCategory,
    language: String,
    levels: PersistentList<String>,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier,
            text = title,
            style = MaterialTheme.typography.headlineSmall,
        )

        Spacer(modifier = Modifier.padding(24.dp))

        SessionTagsLine(
            startsAt = startsAt,
            endsAt = endsAt,
            room = room,
            category = category,
            levels = levels
        )

        Spacer(modifier = Modifier.padding(24.dp))

        SessionScheduleInfo(
            startTime = startsAt,
            endTime = endsAt,
            modifier = Modifier
        )
        // TODO favorite button
    }
}

@Composable
fun SessionDetailDescription(
    modifier: Modifier = Modifier,
    description: String,
) {
    var isReadMore by remember { mutableStateOf(false) }
    var isOverFlow by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            modifier = Modifier.animateContentSize(),
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = if (isReadMore) Int.MAX_VALUE else 5,
            overflow = if (isReadMore) TextOverflow.Visible else TextOverflow.Ellipsis,
            onTextLayout = { result ->
                isOverFlow = result.isLineEllipsized(result.lineCount - 1)
            }
        )
        if (isOverFlow) {
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                modifier = Modifier.clickable {
                    isReadMore = true
                },
                text = stringResource(id = R.string.session_description_read_more_text),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6EFD9E),
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun SessionDetailTargetAudience(
    modifier: Modifier = Modifier,
    targetAudience: String,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier,
            text = stringResource(id = R.string.session_target_audience),
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Text(
            modifier = Modifier,
            text = targetAudience,
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun SessionDetailSpeakers(
    modifier: Modifier = Modifier,
    speakers: List<TimetableSpeaker>,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier,
            text = stringResource(id = R.string.session_speaker),
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.padding(16.dp))

        speakers.forEach { speaker ->
            if (speaker.iconUrl.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AsyncImage(
                        model = speaker.iconUrl,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center,
                        contentDescription = "Speaker Icon",
                    )
                    Spacer(
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                    // TODO Transition to Speaker detail
                    Text(
                        modifier = Modifier,
                        text = speaker.name,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                Spacer(
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.padding(16.dp),
        )

        Spacer(
            modifier = Modifier.padding(vertical = 16.dp),
        )
    }
}

@Composable
fun SessionDetailAssets(
    modifier: Modifier = Modifier,
    asset: TimetableAsset,
) {
    val uriHandler = LocalUriHandler.current

    Column(modifier = modifier) {
        Text(
            modifier = Modifier,
            text = stringResource(id = R.string.session_material),
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.padding(16.dp))

        SessionDetailAssetsItem(
            modifier = Modifier,
            painter = painterResource(id = R.drawable.ic_video_cam),
            text = stringResource(id = R.string.session_movie),
            onClick = {
                val videoUrl = asset.videoUrl
                if (videoUrl != null) {
                    uriHandler.openUri(videoUrl)
                }
            },
        )

        Spacer(modifier = Modifier.padding(8.dp))

        SessionDetailAssetsItem(
            modifier = Modifier,
            painter = painterResource(id = R.drawable.ic_photo_library),
            text = stringResource(id = R.string.session_slide),
            onClick = {
                val slideUrl = asset.slideUrl
                if (slideUrl != null) {
                    uriHandler.openUri(slideUrl)
                }
            },
        )
    }
}

@Composable
private fun SessionDetailAssetsItem(
    modifier: Modifier = Modifier,
    painter: Painter,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .height(36.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painter,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier,
            text = text,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSessionDetailScreen() {
    KaigiTheme {
        SessionDetailScreen(
            uiModel = SessionDetailUiModel(
                Loaded(TimetableItemWithFavorite.fake())
            )
        )
    }
}
