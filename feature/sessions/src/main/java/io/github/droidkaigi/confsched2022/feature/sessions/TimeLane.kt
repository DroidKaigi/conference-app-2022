package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import io.github.droidkaigi.confsched2022.model.TimetableItemWithFavorite

@Composable
fun TimeLane(
    timetable: List<Pair<DurationTime, TimetableItemWithFavorite>>,
    sessionsListListState: LazyListState,
    modifier: Modifier = Modifier,
    content: @Composable (DurationTime) -> Unit,
) {
    val visibleItemsInfo = remember {
        derivedStateOf {
            sessionsListListState.layoutInfo.visibleItemsInfo
        }
    }
    var currentDurationTime: DurationTime? = null

    if (visibleItemsInfo.value.isEmpty()) {
        // For preview
        val durationTime = timetable[0].first
        Box(
            modifier = modifier.offset {
                IntOffset(
                    x = 0,
                    y = 0,
                )
            }
        ) {
            content(durationTime)
        }
        return
    }

    visibleItemsInfo.value.forEachIndexed { visibleItemIndex, visibleItemInfo ->
        val durationTime = timetable[visibleItemInfo.index].first
        if (currentDurationTime != durationTime) {
            currentDurationTime = durationTime
            val nextDurationTime = timetable.getOrNull(visibleItemInfo.index + 1)?.first
            Box(
                modifier = modifier.offset {
                    IntOffset(
                        x = 0,
                        y = if (visibleItemIndex == 0 &&
                            durationTime == nextDurationTime
                        ) 0 else visibleItemInfo.offset,
                    )
                }
            ) {
                content(durationTime)
            }
        }
    }
}
