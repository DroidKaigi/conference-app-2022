package io.github.droidkaigi.confsched2022

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched2022.model.Timetable
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableRoom
import io.github.droidkaigi.confsched2022.model.fake
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Timetable(
    timetable: Timetable,
    modifier: Modifier = Modifier,
    content: @Composable (TimetableItem, Boolean) -> Unit,
) {
    val itemProvider = itemProvider({ timetable.timetableItems.size }) { index ->
        val timetableItem = timetable.contents[index]
        content(timetableItem.timetableItem, timetableItem.isFavorited)
    }
    val density = LocalDensity.current
    val timetableLayout = remember(timetable) {
        TimetableLayout(timetable = timetable, density = density)
    }
    val screenScroll = remember {
        ScreenScroll()
    }
    val screen = remember(timetableLayout) {
        Screen(
            timetableLayout,
            0,
            0,
            screenScroll
        )
    }
    val scrollableYState = rememberScrollableState(consumeScrollDelta = { scrollY: Float ->
        screen.scrollY(scrollY)
    })
    val scrollableXState = rememberScrollableState(consumeScrollDelta = { scrollY: Float ->
        screen.scrollX(scrollY)
    })
    val visibleItemLayouts by remember(screen) { screen.visibleItemLayouts }
    LazyLayout(
        modifier = modifier
            .drawBehind {
                screen.lines.value.forEach {
                    drawLine(
                        Color.LightGray,
                        Offset(0F, it.toFloat()),
                        Offset(screen.width.toFloat(), it.toFloat())
                    )
                }
            }
            .scrollable(
                orientation = Orientation.Vertical,
                state = scrollableYState,
                flingBehavior = ScrollableDefaults.flingBehavior()
            )
            .scrollable(
                orientation = Orientation.Horizontal,
                state = scrollableXState,
                flingBehavior = ScrollableDefaults.flingBehavior()
            ),
        itemProvider = itemProvider
    ) { constraint ->

        data class ItemData(val placeable: Placeable, val timetableItem: TimetableItemLayout)
        screen.height = constraint.maxHeight
        screen.width = constraint.maxWidth

        val items = visibleItemLayouts.map { (index, timetableLayout) ->
            ItemData(
                placeable = measure(
                    index,
                    Constraints.fixed(
                        width = timetableLayout.width,
                        height = timetableLayout.height
                    )
                )[0],
                timetableItem = timetableLayout
            )
        }
        layout(constraint.maxWidth, constraint.maxHeight) {
            items.forEach { (placable, timetableLayout) ->
                placable.place(
                    timetableLayout.left + screen.screenScroll.scrollX.value,
                    timetableLayout.top + screen.screenScroll.scrollY.value
                )
            }
        }
    }
}

@Preview
@Composable
fun TimetablePreview() {
    Timetable(
        modifier = Modifier.fillMaxSize(),
        timetable = Timetable.fake()
    ) { timetableItem, isFavorite ->
        TimetableItem(timetableItem, isFavorite)
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun itemProvider(
    itemCount: () -> Int,
    itemContent: @Composable (Int) -> Unit
): LazyLayoutItemProvider {
    return object : LazyLayoutItemProvider {
        @Composable
        override fun Item(index: Int) {
            itemContent(index)
        }

        override val itemCount: Int get() = itemCount()
    }
}

private data class TimetableItemLayout(
    val timetableItem: TimetableItem,
    val rooms: List<TimetableRoom>,
    val dayStartTime: Instant,
    val density: Density,
    val minutePx: Int
) {
    val height = (timetableItem.endsAt - timetableItem.startsAt)
        .inWholeMinutes.toInt() * minutePx
    val width = with(density) {
        80.dp.roundToPx()
    }
    val left = rooms.indexOf(timetableItem.room) * width
    val top = (timetableItem.startsAt - dayStartTime)
        .inWholeMinutes.toInt() * minutePx
    val right = left + width
    val bottom = top + height

    fun isVisible(
        screenWidth: Int,
        screenHeight: Int,
        scrollX: Int,
        scrollY: Int
    ): Boolean {
        val screenLeft = -scrollX
        val screenRight = -scrollX + screenWidth
        val screenTop = -scrollY
        val screenBottom = -scrollY + screenHeight
        val xInside =
            left in screenLeft..screenRight || right in screenLeft..screenRight
        val yInside =
            top in screenTop..screenBottom || bottom in screenTop..screenBottom
        return xInside && yInside
    }
}

private data class TimetableLayout(val timetable: Timetable, val density: Density) {
    val rooms = timetable.timetableItems.map { it.room }.toSet().sortedBy { it.sort }
    val dayStartTime = timetable.timetableItems.minOfOrNull { it.startsAt }
    var timetableHeight = 0
    var timetableWidth = 0
    val minutePx = with(density) {
        4.dp.roundToPx()
    }
    val timetableLayouts = timetable.timetableItems.map {
        val timetableItemLayout = TimetableItemLayout(
            timetableItem = it,
            rooms = rooms,
            dayStartTime = dayStartTime!!,
            density = density,
            minutePx = minutePx
        )
        timetableHeight =
            maxOf(timetableHeight, timetableItemLayout.bottom)
        timetableWidth =
            maxOf(timetableWidth, timetableItemLayout.right)
        timetableItemLayout
    }

    fun visibleItemLayouts(
        screenWidth: Int,
        screenHeight: Int,
        scrollX: Int,
        scrollY: Int
    ): List<IndexedValue<TimetableItemLayout>> {
        return timetableLayouts.withIndex().filter { (_, layout) ->
            layout.isVisible(screenWidth, screenHeight, scrollX, scrollY)
        }
    }
}

private data class ScreenScroll(
    val scrollX: MutableState<Int> = mutableStateOf(0),
    val scrollY: MutableState<Int> = mutableStateOf(0)
)

private class Screen(
    val timetableLayout: TimetableLayout,
    var width: Int,
    var height: Int,
    val screenScroll: ScreenScroll
) {
    val visibleItemLayouts: State<List<IndexedValue<TimetableItemLayout>>> =
        derivedStateOf {
            timetableLayout.visibleItemLayouts(
                width,
                height,
                screenScroll.scrollX.value,
                screenScroll.scrollY.value
            )
        }
    val lines = derivedStateOf {
        val startTime = timetableLayout.dayStartTime ?: return@derivedStateOf listOf()
        val startMinute = startTime.toLocalDateTime((TimeZone.currentSystemDefault())).minute
        (0..10).map {
            val minuteOffSet = startMinute * timetableLayout.minutePx
            screenScroll.scrollY.value + timetableLayout.minutePx * 60 * it - minuteOffSet
        }
    }

    override fun toString(): String {
        return "Screen(" +
            "width=$width, " +
            "height=$height, " +
            "scroll=$screenScroll, " +
            "visibleItemLayouts=$visibleItemLayouts" +
            ")"
    }

    fun scrollX(scrollX: Float): Float {
        val currentValue = this.screenScroll.scrollX.value
        val nextValue = currentValue + scrollX
        val maxScroll = if (width < timetableLayout.timetableWidth) {
            -(timetableLayout.timetableWidth - width)
        } else {
            0
        }
        val nextPossibleValue = maxOf(minOf(nextValue.toInt(), 0), maxScroll)
        this.screenScroll.scrollX.value = nextPossibleValue
        return nextPossibleValue.toFloat() - currentValue
    }

    fun scrollY(scrollY: Float): Float {
        val currentValue = this.screenScroll.scrollY.value
        val nextValue = currentValue + scrollY
        val maxScroll =
            if (height < timetableLayout.timetableHeight) {
                -(timetableLayout.timetableHeight - height)
            } else {
                0
            }
        val nextPossibleValue = maxOf(minOf(nextValue.toInt(), 0), maxScroll)
        this.screenScroll.scrollY.value = nextPossibleValue
        return nextPossibleValue.toFloat() - currentValue
    }
}
