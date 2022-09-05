package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.feature.sessions.SessionDetailUiModel.SessionDetailState.Loaded
import io.github.droidkaigi.confsched2022.model.TimetableAsset
import io.github.droidkaigi.confsched2022.model.TimetableCategory
import io.github.droidkaigi.confsched2022.model.TimetableItem.Session
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.TimetableItemWithFavorite
import io.github.droidkaigi.confsched2022.model.TimetableRoom
import io.github.droidkaigi.confsched2022.model.TimetableSpeaker
import io.github.droidkaigi.confsched2022.model.fake
import kotlinx.collections.immutable.PersistentList
import kotlinx.datetime.Instant

@Composable
fun SessionDetailScreenRoot(
    modifier: Modifier = Modifier,
    timetableItemId: TimetableItemId,
    onNavigationIconClick: () -> Unit = {}
) {

    val viewModel = hiltViewModel<SessionDetailViewModel>()
    val uiModel by viewModel.uiModel

    SessionDetailScreen(
        uiModel = uiModel,
        onFavoriteClick = { currentFavorite ->
            viewModel.onFavoriteToggle(timetableItemId, currentFavorite)
        }
    )
}

@Composable
fun SessionDetailScreen(
    modifier: Modifier = Modifier,
    uiModel: SessionDetailUiModel,
    onNavigationIconClick: () -> Unit = {},
    onFavoriteClick: (Boolean) -> Unit = {},
) {
    if (uiModel.sessionDetailState !is Loaded) {
        CircularProgressIndicator()
        return
    }
    val (item, isFavorited) = uiModel.sessionDetailState.timetableItemWithFavorite
    KaigiScaffold(
        topBar = {
            KaigiTopAppBar(
                onNavigationIconClick = onNavigationIconClick,
            )
        },
        bottomBar = {
            BottomAppBar {
                Row {
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
            modifier = modifier.verticalScroll(rememberScrollState())
        ) {
            SessionDetailSessionInfo(
                title = item.title.currentLangTitle,
                startsAt = item.startsAt,
                endsAt = item.startsAt,
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
    Column {
        Text(
            modifier = modifier,
            text = title,
            style = MaterialTheme.typography.headlineSmall,
        )

        Spacer(modifier = modifier.padding(16.dp))

        Text(
            modifier = modifier,
            text = "$startsAt $endsAt",
            style = MaterialTheme.typography.bodySmall,
        )

        Text(
            modifier = modifier,
            text = room.name.currentLangTitle,
            style = MaterialTheme.typography.bodySmall,
        )

        // TODO TagLines
        // TODO favorite button
    }
}

@Composable
fun SessionDetailDescription(
    modifier: Modifier = Modifier,
    description: String,
) {
    val (isReadMore, setIsReadMore) = remember { mutableStateOf(false) }
    val (isOverFlow, setIsOverFlow) = remember { mutableStateOf(false) }
    Column {
        Spacer(modifier = modifier.padding(16.dp))
        // TODO expand by amount of text
        Text(
            modifier = modifier.animateContentSize(),
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = if (isReadMore) Int.MAX_VALUE else 5,
            overflow = if (isReadMore) TextOverflow.Visible else TextOverflow.Ellipsis,
            onTextLayout = { result ->
                setIsOverFlow(result.isLineEllipsized(result.lineCount - 1))
            }
        )
        if (isOverFlow) {
            Spacer(modifier = modifier.padding(8.dp))
            Text(
                modifier = modifier.clickable {
                    setIsReadMore(true)
                },
                text = stringResource(id = R.string.session_description_read_more_text),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6EFD9E),
            )
        }
        Spacer(modifier = modifier.padding(16.dp))
    }
}

@Composable
fun SessionDetailTargetAudience(
    modifier: Modifier = Modifier,
    targetAudience: String,
) {
    Column {
        Text(
            modifier = modifier,
            text = "対象者",
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = modifier.padding(16.dp))

        Text(
            modifier = modifier,
            text = targetAudience,
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = modifier.padding(16.dp))
    }
}

@Composable
fun SessionDetailSpeakers(
    modifier: Modifier = Modifier,
    speakers: List<TimetableSpeaker>,
) {
    Column {
        Text(
            modifier = modifier,
            text = "スピーカー",
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = modifier.padding(16.dp))

        speakers.forEach { speaker ->
            if (speaker.iconUrl.isNotEmpty()) {
                Row {
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
                        modifier = modifier.padding(horizontal = 16.dp),
                    )
                    // TODO Transition to Speaker detail
                    Text(
                        modifier = modifier,
                        text = speaker.name,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }

        Spacer(
            modifier = modifier.padding(16.dp),
        )

        Spacer(
            modifier = modifier.padding(vertical = 16.dp),
        )
    }
}

@Composable
fun SessionDetailAssets(
    modifier: Modifier = Modifier,
    asset: TimetableAsset,
) {
    Column {
        Text(
            modifier = modifier,
            text = "資料",
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = modifier.padding(16.dp))

        Text(
            modifier = modifier,
            text = "MOVIE",
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = modifier.padding(8.dp))

        Text(
            modifier = modifier,
            text = "スライド",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
fun PreviewSessionDetailScreen() {
    SessionDetailScreen(
        uiModel = SessionDetailUiModel(
            Loaded(TimetableItemWithFavorite.fake())
        )
    )
}
