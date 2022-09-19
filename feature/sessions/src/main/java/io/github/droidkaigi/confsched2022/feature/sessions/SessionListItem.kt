package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTag
import io.github.droidkaigi.confsched2022.designsystem.theme.TimetableItemColor
import io.github.droidkaigi.confsched2022.feature.sessions.R.drawable
import io.github.droidkaigi.confsched2022.model.Lang
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItem.Session
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.secondLang

@Composable
fun SessionListItem(
    timetableItem: TimetableItem,
    isFavorited: Boolean,
    onFavoriteClick: (TimetableItemId, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    maxTitleLines: Int = 4,
    searchWord: String? = null,
) {
    val roomName = timetableItem.room.name
    val roomColor = Color(TimetableItemColor.colorOfRoomName(enName = roomName.enTitle))
    val lang = Lang.valueOf(timetableItem.language.langOfSpeaker)
    val secondLang = lang.secondLang()

    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1F)) {
            if (searchWord.isNullOrEmpty()) {
                Text(
                    text = timetableItem.title.currentLangTitle,
                    color = Color.White,
                    modifier = Modifier,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = maxTitleLines,
                    style = MaterialTheme.typography.titleLarge
                )
            } else {
                HighlightedText(
                    text = timetableItem.title.currentLangTitle,
                    keyword = searchWord,
                    maxTitleLines = maxTitleLines,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (timetableItem is Session) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AsyncImage(
                        model = timetableItem.speakers.first().iconUrl,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape),
                        placeholder = painterResource(drawable.ic_baseline_person_24),
                        error = painterResource(drawable.ic_baseline_person_24),
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center,
                        contentDescription = "Speaker Icon",
                    )
                    Text(
                        modifier = Modifier,
                        text = timetableItem.speakers.first().name,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KaigiTag(backgroundColor = roomColor) { Text(roomName.enTitle) }
                KaigiTag(
                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(lang.tagName)
                }
                if (timetableItem.language.isInterpretationTarget &&
                    secondLang != null
                ) {
                    KaigiTag(
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    ) {
                        Text(secondLang.tagName)
                    }
                }
                KaigiTag(
                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer
                ) { Text(timetableItem.minutesString) }
            }
        }
        IconButton(
            modifier = Modifier
                .testTag("favorite")
                .semantics {
                    stateDescription = if (isFavorited) "ON" else "OFF"
                },
            onClick = { onFavoriteClick(timetableItem.id, isFavorited) }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    id = if (isFavorited) {
                        drawable.ic_bookmark_filled
                    } else {
                        drawable.ic_bookmark
                    }
                ),
                contentDescription = "favorite",
            )
        }
    }
}

@Composable
private fun HighlightedText(
    text: String,
    keyword: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSecondary,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    maxTitleLines: Int = 4,
) {
    val highlightStyle = SpanStyle(color = color, background = backgroundColor)
    val annotatedText = remember(text, keyword) {
        buildAnnotatedString {
            append(text)
            if (keyword.isNotEmpty()) {
                var index = text.indexOf(keyword)
                while (index >= 0) {
                    addStyle(highlightStyle, index, index + keyword.length)
                    index = text.indexOf(keyword, index + 1)
                }
            }
        }
    }
    Text(
        text = annotatedText,
        color = Color.White,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis,
        maxLines = maxTitleLines,
        style = MaterialTheme.typography.titleLarge
    )
}
