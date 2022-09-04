package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun HoursItem(
    modifier: Modifier = Modifier,
    hour: String
) {
    Text(
        text = hour,
        color = Color.White,
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Hours(
    modifier: Modifier = Modifier,
    timetableState: TimetableState,
    content: @Composable (Modifier, String) -> Unit,
) {
    val itemProvider = itemProvider({ hoursList.size }) { index ->
        content(modifier, hoursList[index])
    }
    val density = timetableState.density
    val hoursLayout = remember(hoursList) {
        HoursLayout(
            hours = hoursList,
            density = density,
        )
    }
    val coroutineScope = rememberCoroutineScope()
    val screenScroll = timetableState.screenScrollState
    val hoursScreen = remember(hoursLayout, density) {
        HoursScreen(
            hoursLayout,
            screenScroll,
            density
        )
    }
    val visibleItemLayouts by remember(hoursScreen) { hoursScreen.visibleItemLayouts }
    val lineColor = MaterialTheme.colorScheme.surfaceVariant
    val linePxSize = with(timetableState.density) { lineStrokeSize.toPx() }
    val lineOffset = with(density) { 67.dp.roundToPx() }
    val lineEnd = with(density) { hoursWidth.roundToPx() }
    LazyLayout(
        modifier = modifier
            .width(hoursWidth)
            .clipToBounds()
            .drawBehind {
                hoursScreen.timeHorizontalLines.value.forEach {
                    drawLine(
                        lineColor,
                        Offset(lineOffset.toFloat(), it),
                        Offset(lineEnd.toFloat(), it),
                        linePxSize
                    )
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
//                        if (hoursScreen.enableHorizontalScroll(dragAmount.x)) {
//                            if (change.positionChange() != Offset.Zero) change.consume()
//                        }
                        if (hoursScreen.enableVerticalScroll(dragAmount.y)) {
                            if (change.positionChange() != Offset.Zero) change.consume()
                        }
                        coroutineScope.launch {
                            hoursScreen.scroll(
                                dragAmount,
                                change.uptimeMillis,
                                change.position
                            )
                        }
                    },
                    onDragCancel = {
                        screenScroll.resetTracking()
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            screenScroll.flingIfPossible()
                        }
                    }
                )
            },
        itemProvider = itemProvider
    ) { constraint ->

        data class ItemData(val placeable: Placeable, val hoursItem: HoursItemLayout)
        hoursScreen.updateBounds(width = constraint.maxWidth, height = constraint.maxHeight)

        val items = visibleItemLayouts.map { (index, hoursLayout) ->
            ItemData(
                placeable = measure(
                    index,
                    Constraints.fixed(
                        width = hoursLayout.width,
                        height = hoursLayout.height
                    )
                )[0],
                hoursItem = hoursLayout
            )
        }
        layout(constraint.maxWidth, constraint.maxHeight) {
            items.forEach { (placable, hoursLayout) ->
                placable.place(
                    hoursLayout.left,
                    hoursLayout.top + hoursScreen.scrollState.scrollY.toInt()
                )
            }
        }
    }
}

private data class HoursLayout(
    val hours: List<String>,
    val density: Density,
) {
    var hoursHeight = 0
    var hoursWidth = 0
    val minutePx = with(density) { (4.23).dp.roundToPx() }
    val hoursLayouts = hours.mapIndexed { index, it ->
        val hoursItemLayout = HoursItemLayout(
            index = index,
            density = density,
            minutePx = minutePx
        )
        hoursHeight =
            maxOf(hoursHeight, hoursItemLayout.bottom)
        hoursWidth =
            maxOf(hoursWidth, hoursItemLayout.width)
        hoursItemLayout
    }

    fun visibleItemLayouts(
        screenHeight: Int,
        scrollY: Int
    ): List<IndexedValue<HoursItemLayout>> {
        return hoursLayouts.withIndex().filter { (_, layout) ->
            layout.isVisible(screenHeight, scrollY)
        }
    }
}

private data class HoursItemLayout(
    val density: Density,
    val minutePx: Int,
    val index: Int
) {
    val topOffset = with(density) { horizontalLineTopOffset.roundToPx() }
    val itemOffset = with(density) { hoursItemTopOffset.roundToPx() }
    val height = minutePx * 60
    val width = with(density) { hoursWidth.roundToPx() }
    val left = 0
    val top = index * height + topOffset - itemOffset
    val right = left + width
    val bottom = top + height

    fun isVisible(
        screenHeight: Int,
        scrollY: Int
    ): Boolean {
        val screenTop = -scrollY
        val screenBottom = -scrollY + screenHeight
        val yInside =
            top in screenTop..screenBottom || bottom in screenTop..screenBottom
        return yInside
    }
}

private class HoursScreen(
    val hoursLayout: HoursLayout,
    val scrollState: ScreenScrollState,
    private val density: Density,
) {
    var width = 0
        private set
    var height = 0
        private set

    val visibleItemLayouts: State<List<IndexedValue<HoursItemLayout>>> =
        derivedStateOf {
            hoursLayout.visibleItemLayouts(
                height,
                scrollState.scrollY.toInt()
            )
        }

    val offset = with(density) { horizontalLineTopOffset.roundToPx() }
    val timeHorizontalLines = derivedStateOf {
        (0..10).map {
            scrollState.scrollY + hoursLayout.minutePx * 60 * it + offset
        }
    }

    override fun toString(): String {
        return "Screen(" +
            "width=$width, " +
            "height=$height, " +
            "scroll=$scrollState, " +
            "visibleItemLayouts=$visibleItemLayouts" +
            ")"
    }

    suspend fun scroll(
        dragAmount: Offset,
        timeMillis: Long,
        position: Offset,
    ) {
        val nextPossibleY = calculatePossibleScrollY(dragAmount.y)
        scrollState.scroll(
            Offset(0F, nextPossibleY),
            timeMillis,
            position
        )
    }

    fun enableHorizontalScroll(dragX: Float): Boolean {
        val nextPossibleX = calculatePossibleScrollX(dragX)
        return (scrollState.maxX < nextPossibleX && nextPossibleX < 0f)
    }

    fun enableVerticalScroll(dragY: Float): Boolean {
        val nextPossibleY = calculatePossibleScrollY(dragY)
        return (scrollState.maxY < nextPossibleY && nextPossibleY < 0f)
    }

    fun updateBounds(width: Int, height: Int) {
        this.width = width
        this.height = height
        scrollState.updateBounds(
            maxX = if (width < hoursLayout.hoursWidth) {
                -(hoursLayout.hoursWidth - width).toFloat()
            } else {
                0f
            },
            maxY = if (height < hoursLayout.hoursHeight) {
                -(hoursLayout.hoursHeight - height).toFloat()
            } else {
                0f
            }
        )
    }

    private fun calculatePossibleScrollX(scrollX: Float): Float {
        val currentValue = scrollState.scrollX
        val nextValue = currentValue + scrollX
        val maxScroll = scrollState.maxX
        return maxOf(minOf(nextValue, 0f), maxScroll)
    }

    private fun calculatePossibleScrollY(scrollY: Float): Float {
        val currentValue = scrollState.scrollY
        val nextValue = currentValue + scrollY
        val maxScroll = scrollState.maxY
        return maxOf(minOf(nextValue, 0f), maxScroll)
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

/**
 * Workaround to prevent detectDragGestures from consuming events by default and disabling parent scrolling.
 *
 * ref: https://stackoverflow.com/a/72935823
 */
private suspend fun PointerInputScope.detectDragGestures(
    onDragStart: (Offset) -> Unit = { },
    onDragEnd: () -> Unit = { },
    onDragCancel: () -> Unit = { },
    onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit
) {
    forEachGesture {
        awaitPointerEventScope {
            val down = awaitFirstDown(requireUnconsumed = false)
            var drag: PointerInputChange?
            val overSlop = Offset.Zero
            do {
                drag = awaitTouchSlopOrCancellation(down.id, onDrag)
                // ! EVERY Default movable GESTURE HAS THIS CHECK
            } while (drag != null && !drag.isConsumed)
            if (drag != null) {
                onDragStart.invoke(drag.position)
                onDrag(drag, overSlop)
                if (
                    !drag(drag.id) {
                        onDrag(it, it.positionChange())
                        it.consume()
                    }
                ) {
                    onDragCancel()
                } else {
                    onDragEnd()
                }
            }
        }
    }
}

private val lineStrokeSize = 1.dp
private val horizontalLineTopOffset = 16.dp
private val hoursWidth = 75.dp
private val hoursItemWidth = 43.dp
private val hoursItemHeight = 24.dp
private val hoursItemTopOffset = 11.dp
private val hoursItemEndOffset = 16.dp
private val hoursList = listOf(
    "10:00",
    "11:00",
    "12:00",
    "13:00",
    "14:00",
    "15:00",
    "16:00",
    "17:00",
    "18:00",
    "19:00",
)
