package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.feature.sessions.TimeTableDetailUiModel.TimetableDetailState.Loaded
import io.github.droidkaigi.confsched2022.model.TimetableAsset
import io.github.droidkaigi.confsched2022.model.TimetableCategory
import io.github.droidkaigi.confsched2022.model.TimetableItem.Session
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.TimetableRoom
import io.github.droidkaigi.confsched2022.model.TimetableSpeaker
import io.github.droidkaigi.confsched2022.model.fake
import kotlinx.collections.immutable.PersistentList
import kotlinx.datetime.Instant

@Composable
fun TimetableDetailScreenRoot(
    modifier: Modifier = Modifier,
    timetableItemId: TimetableItemId,
    onNavigationIconClick: () -> Unit = {}
) {

    val viewModel by viewModel<TimeTableDetailViewModel>(
        factory = TimeTableDetailViewModel.provideFactory(
            assistedFactory = viewModelFactory,
            timetableItemId = timetableItemId,
        )
    )
    val uiModel by viewModel.uiModel

    TimetableDetailScreen(
        uiModel = uiModel
    )

    // TODO BottomNavigationView
}

@Composable
fun TimetableDetailScreen(
    modifier: Modifier = Modifier,
    uiModel: TimeTableDetailUiModel,
    onNavigationIconClick: () -> Unit = {},
) {
    if (uiModel.timetableDetailState !is Loaded) {
        CircularProgressIndicator()
        return
    }
    val item = uiModel.timetableDetailState.timetableItem
    KaigiScaffold(
        topBar = {
            KaigiTopAppBar(
                onNavigationIconClick = onNavigationIconClick,
            )
        }
    ) {
        Column(
            modifier = modifier.verticalScroll(rememberScrollState())
        ) {
            TimetableDetailSessionInfo(
                title = item.title.currentLangTitle,
                startsAt = item.startsAt,
                endsAt = item.startsAt,
                room = item.room,
                category = item.category,
                language = item.language,
                levels = item.levels,
            )

            if (item is Session)
                TimetableDetailDescription(
                    description = item.description
                )

            TimetableDetailTargetAudience(
                targetAudience = item.targetAudience
            )

            if (item is Session)
                TimetableDetailSpeakers(
                    speakers = item.speakers,
                )
            TimetableDetailAssets(
                asset = item.asset
            )
        }
    }
}

@Composable
fun TimetableDetailSessionInfo(
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

        // TODO タグリスト
        // TODO いいねボタン
    }
}

@Composable
fun TimetableDetailDescription(
    modifier: Modifier = Modifier,
    description: String,
) {
    Column {
        Spacer(modifier = modifier.padding(16.dp))
        // TODO expand by amount of text
        Text(
            modifier = modifier,
            text = description,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = modifier.padding(16.dp))
    }
}

@Composable
fun TimetableDetailTargetAudience(
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
fun TimetableDetailSpeakers(
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
fun TimetableDetailAssets(
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
fun PreviewTimetableDetailScreen() {
    TimetableDetailScreen(
        uiModel = TimeTableDetailUiModel(Loaded(Session.fake()))
    )
}
