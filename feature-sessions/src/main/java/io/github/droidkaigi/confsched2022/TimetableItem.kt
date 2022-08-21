package io.github.droidkaigi.confsched2022

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched2022.model.TimetableItem

@Composable
fun TimetableItem(
    timetableItem: TimetableItem,
    isFavorited: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isFavorited) {
        MaterialTheme.colorScheme.tertiaryContainer
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }
    Column(
        modifier
            .background(backgroundColor, MaterialTheme.shapes.small)
            .padding(4.dp)
    ) {
        Text(timetableItem.startsTimeString)
        Text(timetableItem.title.currentLangTitle)
        if (timetableItem is TimetableItem.Session) {
            Text(timetableItem.speakers.joinToString { it.name })
        }
    }
}
