package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import io.github.droidkaigi.confsched2022.model.TimetableItemWithFavorite
import kotlinx.collections.immutable.ImmutableList

@Composable
fun TimeLane(
    timetable: ImmutableList<Pair<DurationTime, TimetableItemWithFavorite>>,
    visibleItemIndices: ImmutableList<Int>,
    getVisibleItemOffset: (index: Int) -> Int,
    modifier: Modifier = Modifier,
    content: @Composable (DurationTime) -> Unit,
) {
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

    visibleItemIndices.forEachIndexed { relativeIndex, absoluteIndex ->
        val durationTime = timetable[absoluteIndex].first
        if (currentDurationTime != durationTime) {
            currentDurationTime = durationTime
            val nextDurationTime = timetable.getOrNull(absoluteIndex + 1)?.first
            Box(
                modifier = modifier.offset {
                    IntOffset(
                        x = 0,
                        y = if (relativeIndex == 0 &&
                            durationTime == nextDurationTime
                        ) 0 else getVisibleItemOffset(relativeIndex),
                    )
                }
            ) {
                content(durationTime)
            }
        }
    }
}
