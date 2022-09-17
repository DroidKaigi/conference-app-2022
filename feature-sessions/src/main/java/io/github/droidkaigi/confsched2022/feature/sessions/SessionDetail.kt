package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode.Expand
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTag
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.designsystem.theme.TimetableItemColor
import io.github.droidkaigi.confsched2022.model.Lang
import io.github.droidkaigi.confsched2022.model.TimetableAsset
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItem.Session
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.TimetableItemWithFavorite
import io.github.droidkaigi.confsched2022.model.TimetableSpeaker
import io.github.droidkaigi.confsched2022.model.fake
import io.github.droidkaigi.confsched2022.model.secondLang
import io.github.droidkaigi.confsched2022.strings.Strings
import io.github.droidkaigi.confsched2022.ui.LocalCalendarRegistration
import io.github.droidkaigi.confsched2022.ui.LocalShareManager
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Error
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Loading
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Success
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Locale

@Composable
fun SessionDetailScreenRoot(
    timetableItemId: TimetableItemId,
    modifier: Modifier = Modifier,
    viewModel: SessionDetailViewModel = hiltViewModel(),
    onBackIconClick: () -> Unit = {},
    onNavigateFloorMapClick: () -> Unit,
) {
    val uiModel by viewModel.uiModel

    val shareManager = LocalShareManager.current
    val calendarRegistration = LocalCalendarRegistration.current

    SessionDetailScreen(
        modifier = modifier,
        uiModel = uiModel,
        onBackIconClick = onBackIconClick,
        onFavoriteClick = { currentFavorite ->
            viewModel.onFavoriteToggle(timetableItemId, currentFavorite)
        },
        onShareClick = {
            shareManager.share(
                "${it.title.currentLangTitle}\nhttps://droidkaigi.jp/2022/timetable/${it.id.value}"
            )
        },
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
    uiModel: SessionDetailUiModel,
    modifier: Modifier = Modifier,
    onBackIconClick: () -> Unit = {},
    onFavoriteClick: (Boolean) -> Unit = {},
    onShareClick: (TimetableItem) -> Unit = {},
    onNavigateFloorMapClick: () -> Unit = {},
    onRegisterCalendarClick: (TimetableItem) -> Unit = {},
) {

    val uiState = uiModel.state

    KaigiScaffold(
        topBar = {
            SessionDetailTopAppBar(
                onBackIconClick = onBackIconClick,
            )
        },
        bottomBar = {
            if (uiState is Success) {
                val (item, isFavorite) = uiState.value

                SessionDetailBottomAppBar(
                    item = item,
                    isFavorite = isFavorite,
                    onFavoriteClick = onFavoriteClick,
                    onShareClick = onShareClick,
                    onNavigateFloorMapClick = onNavigateFloorMapClick,
                    onRegisterCalendarClick = onRegisterCalendarClick,
                )
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (uiState) {
                is Error -> TODO()
                Loading ->
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                is Success -> {
                    val (item, _) = uiState.value

                    Column(
                        modifier = modifier
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp)
                    ) {
                        SessionDetailSessionInfo(item = item)

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
        }
    }
}

@Composable
fun SessionDetailBottomAppBar(
    modifier: Modifier = Modifier,
    item: TimetableItem,
    isFavorite: Boolean,
    onFavoriteClick: (Boolean) -> Unit,
    onShareClick: (TimetableItem) -> Unit,
    onNavigateFloorMapClick: () -> Unit,
    onRegisterCalendarClick: (TimetableItem) -> Unit,
) {
    BottomAppBar(modifier = modifier) {
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
                    onFavoriteClick(isFavorite)
                }
            ) {
                if (isFavorite) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark_filled),
                        contentDescription = "favorite"
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark),
                        contentDescription = "not favorite"
                    )
                }
            }
        }
    }
}

@Composable
fun SessionTagsLine(
    modifier: Modifier = Modifier,
    item: TimetableItem
) {
    val sessionMinutes =
        "${(item.endsAt - item.startsAt).toComponents { minutes, _, _ -> minutes }}"
    val roomColor = TimetableItemColor.colorOfRoomName(enName = item.room.name.enTitle)
    val lang = Lang.valueOf(item.language.langOfSpeaker)
    val secondLang = lang.secondLang()

    FlowRow(
        modifier = modifier,
        mainAxisSize = Expand,
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp
    ) {
        KaigiTag(
            backgroundColor = Color(roomColor)
        ) {
            Text(item.room.name.currentLangTitle)
        }
        KaigiTag(
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Text(lang.tagName)
        }
        if (item.language.isInterpretationTarget &&
            secondLang != null
        ) {
            KaigiTag(
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text(
                    secondLang.tagName +
                        stringResource(Strings.session_language_interpretation)
                )
            }
        }
        KaigiTag(
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Text(sessionMinutes + "min")
        }
        KaigiTag(
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Text(item.category.title.currentLangTitle)
        }
        item.levels.forEach {
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
    startTime: Instant,
    endTime: Instant,
    modifier: Modifier = Modifier,
) {
    val sessionStartDateTime = startTime
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val sessionEndDateTime = endTime
        .toLocalDateTime(TimeZone.currentSystemDefault())

    fun LocalDateTime.toTime() = "$hour:${minute.toString().padStart(2, '0')}"

    val japanese = "ja"

    val month = if (Locale.getDefault().language == japanese) {
        "${sessionStartDateTime.monthNumber}月"
    } else {
        sessionStartDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
    }

    val day = if (Locale.getDefault().language == japanese) {
        "${sessionStartDateTime.dayOfMonth}日"
    } else {
        "${sessionStartDateTime.dayOfMonth}th"
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painterResource(id = R.drawable.ic_schedule),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "$month $day ${sessionStartDateTime.toTime()}-${sessionEndDateTime.toTime()}",
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
fun SessionDetailSessionInfo(
    item: TimetableItem,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier,
            text = item.title.currentLangTitle,
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(modifier = Modifier.padding(24.dp))

        SessionTagsLine(item = item)

        Spacer(modifier = Modifier.padding(24.dp))

        SessionScheduleInfo(
            startTime = item.startsAt,
            endTime = item.endsAt,
            modifier = Modifier
        )
        // TODO favorite button
    }
}

@Composable
fun SessionDetailDescription(
    description: String,
    modifier: Modifier = Modifier,
) {
    var isReadMore by remember { mutableStateOf(false) }
    var isOverFlow by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            modifier = modifier
                .animateContentSize()
                .clickable {
                    isReadMore = true
                },
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
                text = stringResource(Strings.session_description_read_more_text),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6EFD9E),
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun SessionDetailTargetAudience(
    targetAudience: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier,
            text = stringResource(Strings.session_target_audience),
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
    speakers: List<TimetableSpeaker>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier,
            text = stringResource(Strings.session_speaker),
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.padding(16.dp))

        speakers.forEach { speaker ->
            Row(
                modifier = Modifier
                    .clearAndSetSemantics {
                        contentDescription = speaker.name
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = speaker.iconUrl,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    placeholder = painterResource(R.drawable.ic_baseline_person_24),
                    error = painterResource(R.drawable.ic_baseline_person_24),
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
    asset: TimetableAsset,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current

    Column(modifier = modifier) {
        Text(
            modifier = Modifier,
            text = stringResource(Strings.session_material),
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.padding(16.dp))

        SessionDetailAssetsItem(
            modifier = Modifier,
            painter = painterResource(id = R.drawable.ic_video_cam),
            text = stringResource(Strings.session_movie),
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
            text = stringResource(Strings.session_slide),
            onClick = {
                val slideUrl = asset.slideUrl
                if (slideUrl != null) {
                    uriHandler.openUri(slideUrl)
                }
            },
        )

        Spacer(modifier = Modifier.padding(16.dp))
    }
}

@Composable
private fun SessionDetailAssetsItem(
    painter: Painter,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .height(36.dp)
            .clickable(onClick = onClick)
            .semantics { role = Role.Button },
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
                Success(TimetableItemWithFavorite.fake())
            )
        )
    }
}
