package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched2022.model.DroidKaigi2022Day
import io.github.droidkaigi.confsched2022.model.TimeLine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun HoursItem(
    hour: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = hour,
        color = Color.White,
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Hours(
    timetableState: TimetableState,
    timeLine: TimeLine?,
    coroutineScope: CoroutineScope,
    day: DroidKaigi2022Day,
    modifier: Modifier = Modifier,
    content: @Composable (String) -> Unit,
) {
    val itemProvider = itemProvider({ hoursList.size }) { index ->
        content(hoursList[index])
    }
    val density = timetableState.density
    val verticalScale = timetableState.screenScaleState.verticalScale
    val scrollState = timetableState.screenScrollState
    val hoursLayout = remember(hoursList, verticalScale) {
        HoursLayout(
            hours = hoursList,
            density = density,
            verticalScale = verticalScale,
        )
    }
    val hoursScreen = remember(hoursLayout, timeLine, density) {
        HoursScreen(
            hoursLayout,
            scrollState,
            timeLine,
            day,
            density
        )
    }
    val visibleItemLayouts by remember(hoursScreen) { hoursScreen.visibleItemLayouts }
    val lineColor = MaterialTheme.colorScheme.surfaceVariant
    val linePxSize = with(timetableState.density) { lineStrokeSize.toPx() }
    val lineOffset = with(density) { 67.dp.roundToPx() }
    val lineEnd = with(density) { hoursWidth.roundToPx() }
    val currentTimeLineColor = MaterialTheme.colorScheme.primary
    val currentTimeCircleRadius =
        with(timetableState.density) { TimetableSizes.currentTimeCircleRadius.toPx() }
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
            .drawWithContent {
                drawContent()
                hoursScreen.timeLinePositionY.value?.let {
                    drawCircle(
                        currentTimeLineColor,
                        currentTimeCircleRadius,
                        Offset(lineEnd.toFloat(), it)
                    )
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        if (change.positionChange() != Offset.Zero) change.consume()
                        coroutineScope.launch {
                            hoursScreen.scroll(
                                dragAmount,
                                change.uptimeMillis,
                                change.position
                            )
                        }
                    },
                    onDragCancel = {
                        scrollState.resetTracking()
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            scrollState.flingYIfPossible()
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
    val verticalScale: Float,
) {
    var hoursHeight = 0
    var hoursWidth = 0
    val minutePx = with(density) { TimetableSizes.minuteHeight.times(verticalScale).toPx() }
    val hoursLayouts = List(hours.size) { index ->
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
    val minutePx: Float,
    val index: Int
) {
    val topOffset = with(density) { horizontalLineTopOffset.roundToPx() }
    val itemOffset = with(density) { hoursItemTopOffset.roundToPx() }
    val height = (minutePx * 60).roundToInt()
    val width = with(density) { hoursWidth.roundToPx() }
    val left = 0
    val top = index * height + topOffset - itemOffset
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
    timeLine: TimeLine?,
    day: DroidKaigi2022Day,
    density: Density,
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
    val timeLinePositionY = derivedStateOf {
        val duration = timeLine?.durationFromScheduleStart(day)
        duration?.let {
            scrollState.scrollY + it.inWholeMinutes * hoursLayout.minutePx + offset
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

    fun updateBounds(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    suspend fun scroll(
        dragAmount: Offset,
        timeMillis: Long,
        position: Offset,
    ) {
        val nextPossibleY = calculatePossibleScrollY(dragAmount.y)
        scrollState.scroll(
            scrollX = scrollState.scrollX,
            scrollY = nextPossibleY,
            timeMillis = timeMillis,
            position = position,
        )
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

private val lineStrokeSize = 1.dp
private val horizontalLineTopOffset = 48.dp
private val hoursWidth = 75.dp
private val hoursItemTopOffset = 11.dp
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
