package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTag
import io.github.droidkaigi.confsched2022.designsystem.theme.TimetableItemColor
import io.github.droidkaigi.confsched2022.model.TimetableItem

@Composable
fun TimetableItem(
    timetableItem: TimetableItem,
    isFavorited: Boolean,
    modifier: Modifier = Modifier,
    maxTitleLines: Int = 4
) {
    val roomName = timetableItem.room.name
    val roomColor = TimetableItemColor.colorOfRoomName(enName = roomName.enTitle)
    val color = if (isFavorited) {
        Color(roomColor)
    } else {
        Color(roomColor).copy(alpha = 0.2F)
    }
    Column(
        modifier
            .background(color, MaterialTheme.shapes.medium)
            .border(2.dp, Color(roomColor), MaterialTheme.shapes.medium)
            .padding(8.dp)
            .semantics { contentDescription = "isFavorited$isFavorited" }
            .testTag("favorite")
    ) {
        Text(
            text = timetableItem.title.currentLangTitle,
            color = Color.White,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            overflow = TextOverflow.Ellipsis,
            maxLines = maxTitleLines,
            style = MaterialTheme.typography.titleMedium.copy(
                letterSpacing = 0.1.sp
            )
        )
        KaigiTag { Text(timetableItem.minutesString) }
    }
}
