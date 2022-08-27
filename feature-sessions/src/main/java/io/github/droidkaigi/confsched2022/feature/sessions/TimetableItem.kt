package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched2022.designsystem.theme.TimetableItemColor
import io.github.droidkaigi.confsched2022.model.TimetableItem

@Composable
fun TimetableItem(
    timetableItem: TimetableItem,
    isFavorited: Boolean,
    modifier: Modifier = Modifier
) {
    val roomName = timetableItem.room.name
    val roomColor = TimetableItemColor.colorOfRoomName(enName = roomName.enTitle)
    Column(
        modifier
            .background(Color(roomColor), MaterialTheme.shapes.medium)
            .padding(4.dp)
    ) {
        Text(timetableItem.startsTimeString)
        Text(timetableItem.title.currentLangTitle)
        if (timetableItem is TimetableItem.Session) {
            Text(timetableItem.speakers.joinToString { it.name })
        }
    }
}
