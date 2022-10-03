package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.TimetableItemWithFavorite
import io.github.droidkaigi.confsched2022.strings.Strings

@Composable
fun SessionList(
    timetable: List<Pair<DurationTime, TimetableItemWithFavorite>>,
    sessionsListListState: LazyListState,
    onTimetableClick: (timetableItemId: TimetableItemId) -> Unit,
    onFavoriteClick: (TimetableItemId, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (Pair<DurationTime?, TimetableItemWithFavorite>) -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        TimeLane(
            timetable = timetable,
            sessionsListListState = sessionsListListState
        ) { durationTime ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(start = 12.dp, top = 12.dp)
                    // Remove time semantics so description is set in SessionListItem
                    .clearAndSetSemantics { },
            ) {
                Text(
                    text = durationTime.startAt,
                    style = MaterialTheme.typography.titleMedium
                )
                Box(
                    modifier = Modifier
                        .size(1.dp, 2.dp)
                        .background(MaterialTheme.colorScheme.onBackground)
                ) { }
                Text(
                    text = durationTime.endAt,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = sessionsListListState,
        ) {
            itemsIndexed(timetable) { _, item ->
                key(item.second.timetableItem.id.value) {
                    val actionLabel = stringResource(
                        if (item.second.isFavorited) {
                            Strings.unregister_favorite_action_label
                        } else {
                            Strings.register_favorite_action_label
                        }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTimetableClick(item.second.timetableItem.id) }
                            .padding(12.dp)
                            .padding(start = 85.dp)
                            .semantics(mergeDescendants = true) {
                                customActions = listOf(
                                    CustomAccessibilityAction(
                                        label = actionLabel,
                                        action = {
                                            onFavoriteClick(
                                                item.second.timetableItem.id,
                                                item.second.isFavorited
                                            )
                                            true
                                        }
                                    )
                                )
                            }
                    ) {
                        content(item)
                    }
                }
            }
        }
    }
}
