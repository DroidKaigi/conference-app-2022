package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched2022.model.Timetable
import io.github.droidkaigi.confsched2022.model.TimetableItem

@Composable
fun SessionList(
    timetable: Timetable,
    sessionsListListState: LazyListState,
    modifier: Modifier = Modifier,
    content: @Composable (TimetableItem, Boolean, Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = sessionsListListState
    ) {
        itemsIndexed(timetable.contents) { index, item ->
            key(item.timetableItem.id.value) {
                content(
                    item.timetableItem,
                    item.isFavorited,
                    index
                )
            }
        }
    }
}
