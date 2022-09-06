package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched2022.model.Timetable
import io.github.droidkaigi.confsched2022.model.TimetableItem

@Composable
fun SessionList(
    timetable: Timetable,
    sessionsListListState: LazyListState,
    modifier: Modifier = Modifier,
    content: @Composable (TimetableItem, Boolean) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        state = sessionsListListState
    ) {
        items(timetable.contents, key = { it.timetableItem.id.value }) {
            content(
                it.timetableItem,
                it.isFavorited
            )
        }
    }
}
