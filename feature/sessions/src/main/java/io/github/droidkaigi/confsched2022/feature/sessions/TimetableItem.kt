package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTag
import io.github.droidkaigi.confsched2022.designsystem.theme.TimetableItemColor
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItem.Session

@Composable
fun TimetableItem(
    timetableItem: TimetableItem,
    isFavorited: Boolean,
    verticalScale: Float,
    modifier: Modifier = Modifier
) {
    val roomName = timetableItem.room.name
    val roomColor = TimetableItemColor.colorOfRoomName(enName = roomName.enTitle)
    val color = if (isFavorited) {
        Color(roomColor)
    } else {
        Color(roomColor).copy(alpha = 0.2F)
    }
    val titleTextStyle = MaterialTheme.typography.titleMedium
    val localDensity = LocalDensity.current.let {
        check(titleTextStyle.fontSize.isSp)
        val densityScale = maxOf(verticalScale, 8f / titleTextStyle.fontSize.value)
        Density(it.density * densityScale, it.fontScale)
    }
    CompositionLocalProvider(
        LocalDensity provides localDensity,
    ) {
        Column(
            modifier
                .background(color, MaterialTheme.shapes.medium)
                .border(2.dp, Color(roomColor), MaterialTheme.shapes.medium)
                .padding(8.dp)
                .semantics { contentDescription = "isFavorited$isFavorited" }
                .testTag("favorite")
        ) {
            if (timetableItem is Session && timetableItem.message != null) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "message by the session",
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
            }
            Text(
                text = timetableItem.title.currentLangTitle,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .weight(weight = 1f, fill = false)
                    .fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                style = titleTextStyle.copy(
                    letterSpacing = 0.1.sp
                )
            )
            KaigiTag { Text(timetableItem.minutesString) }
        }
    }
}
