package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import io.github.droidkaigi.confsched2022.model.TimetableItemWithFavorite

@Composable
fun SessionList(
    timetable: List<Pair<DurationTime?, TimetableItemWithFavorite>>,
    sessionsListListState: LazyListState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable (Pair<DurationTime?, TimetableItemWithFavorite>) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = sessionsListListState,
        contentPadding = contentPadding,
    ) {
        itemsIndexed(timetable) { _, item ->
            key(item.second.timetableItem.id.value) {
                content(item)
            }
        }
    }
}
