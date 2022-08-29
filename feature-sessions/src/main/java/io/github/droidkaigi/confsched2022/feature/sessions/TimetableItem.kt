package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.droidkaigi.confsched2022.designsystem.theme.TimetableItemColor
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItemId

@Composable
fun TimetableItem(
    timetableItem: TimetableItem,
    isFavorited: Boolean,
    onFavoriteClick: (TimetableItemId, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    maxTitleLines: Int = 4
) {
    val roomName = timetableItem.room.name
    val roomColor = TimetableItemColor.colorOfRoomName(enName = roomName.enTitle)
    val minutesString = (timetableItem.endsAt - timetableItem.startsAt)
        .toComponents { minutes, _, _ -> minutes.toString() }
    Column(
        modifier
            .clickable(
                onClick = { onFavoriteClick(timetableItem.id, isFavorited) }
            )
            .background(Color(roomColor), MaterialTheme.shapes.medium)
            .padding(8.dp)
    ) {
        Text(
            text = timetableItem.title.currentLangTitle,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            overflow = TextOverflow.Ellipsis,
            maxLines = maxTitleLines,
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                letterSpacing = 0.1.sp,
                color = Color.White
            )
        )
        Text(
            text = stringResource(R.string.timetable_item_minutes, minutesString),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                    color = colorResource(R.color.timetable_item_minutes_background)
                )
                .padding(
                    horizontal = 8.dp,
                    vertical = 6.dp
                ),
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp,
                color = Color.White
            )
        )
    }
}
