package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTag
import io.github.droidkaigi.confsched2022.designsystem.theme.TimetableItemColor
import io.github.droidkaigi.confsched2022.feature.sessions.R.drawable
import io.github.droidkaigi.confsched2022.model.Lang
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.secondLang

@Composable
fun SessionListItem(
    timetableItem: TimetableItem,
    isFavorited: Boolean,
    onFavoriteClick: (TimetableItemId, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    maxTitleLines: Int = 4
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
            Text(
                text = timetableItem.title.currentLangTitle,
                color = Color.White,
                modifier = Modifier,
                overflow = TextOverflow.Ellipsis,
                maxLines = maxTitleLines,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KaigiTag(
                    backgroundColor = Color(lang.backgroundColor)
                ) {
                    Text(lang.tagName)
                }
                if (timetableItem.language.isInterpretationTarget &&
                    secondLang != null
                ) {
                    KaigiTag(
                        backgroundColor = Color(secondLang.backgroundColor)
                    ) {
                        Text(secondLang.tagName)
                    }
                }
                KaigiTag(backgroundColor = roomColor) { Text(roomName.enTitle) }
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
