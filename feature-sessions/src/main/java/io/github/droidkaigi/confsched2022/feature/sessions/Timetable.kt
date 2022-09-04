package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched2022.model.Timetable
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableRoom
import io.github.droidkaigi.confsched2022.model.fake
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class TimetableState(
    val screenScrollState: ScreenScrollState,
//    val screenState: Screen,
)

@Composable
fun rememberTimetableState(
    screenScrollState: ScreenScrollState = rememberScreenScrollState()
): TimetableState = remember {
    TimetableState(screenScrollState)
}

private data class HoursLayout(
    val hours: List<String>,
    val density: Density,
) {
    //    val rooms = timetable.timetableItems.map { it.room }.toSet().sortedBy { it.sort }
//    val dayStartTime = timetable.timetableItems.minOfOrNull { it.startsAt }
    var hoursHeight = 0
    var hoursWidth = 0
    val minutePx = with(density) {
        (4.23).dp.roundToPx()
    }
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

@Composable
fun HoursItem(
    modifier: Modifier = Modifier,
    hour: String
) {
    Text(text = hour, modifier = modifier.padding(top = 64.dp, bottom = 64.dp))
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Hours(
    modifier: Modifier = Modifier,
    hoursList: List<String>,
    timetableState: TimetableState,
    content: @Composable (Modifier, String) -> Unit,
) {
    val itemProvider = itemProvider({ hoursList.size }) { index ->
        content(modifier, hoursList[index])
    }
    val density = LocalDensity.current
    val hoursLayout = remember(hoursList) {
        HoursLayout(hours = hoursList, density = density)
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
//    val lineColor = MaterialTheme.colorScheme.surfaceVariant
//    val linePxSize = with(LocalDensity.current) { timeTableLineStrokeSize.toPx() }
    LazyLayout(
        modifier = modifier
            .width(64.dp)
            .clipToBounds()
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
//                    0,
                    hoursLayout.top + hoursScreen.scrollState.scrollY.toInt()
                )
            }
        }
    }
}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun Timetable(
//    modifier: Modifier = Modifier,
//    timetable: Timetable,
//    timetableState: TimetableState,
//    content: @Composable (TimetableItem, Boolean) -> Unit,
//) {
//    val itemProvider = itemProvider({ timetable.timetableItems.size }) { index ->
//        val timetableItemWithFavorite = timetable.contents[index]
//        content(timetableItemWithFavorite.timetableItem, timetableItemWithFavorite.isFavorited)
//    }
//    val density = LocalDensity.current
//    val timetableLayout = remember(timetable) {
//        TimetableLayout(timetable = timetable, density = density)
//    }
//    val coroutineScope = rememberCoroutineScope()
//    val screenScroll = timetableState.screenScrollState
//    val timetableScreen = remember(timetableLayout, density) {
//        TimetableScreen(
//            timetableLayout,
//            screenScroll,
//            density
//        )
//    }
//    val visibleItemLayouts by remember(timetableScreen) { timetableScreen.visibleItemLayouts }
//    val lineColor = MaterialTheme.colorScheme.surfaceVariant
//    val linePxSize = with(LocalDensity.current) { timeTableLineStrokeSize.toPx() }
//    LazyLayout(
//        modifier = modifier
//            .clipToBounds()
//            .drawBehind {
//                timetableScreen.timeHorizontalLines.value.forEach {
//                    drawLine(
//                        lineColor,
//                        Offset(0F, it),
//                        Offset(timetableScreen.width.toFloat(), it),
//                        linePxSize
//                    )
//                }
//                timetableScreen.roomVerticalLines.value.forEach {
//                    drawLine(
//                        lineColor,
//                        Offset(it, 0f),
//                        Offset(it, timetableScreen.height.toFloat()),
//                        linePxSize
//                    )
//                }
//            }
//            .pointerInput(Unit) {
//                detectDragGestures(
//                    onDrag = { change, dragAmount ->
//                        if (timetableScreen.enableHorizontalScroll(dragAmount.x)) {
//                            if (change.positionChange() != Offset.Zero) change.consume()
//                        }
//                        coroutineScope.launch {
//                            timetableScreen.scroll(
//                                dragAmount,
//                                change.uptimeMillis,
//                                change.position
//                            )
//                        }
//                    },
//                    onDragCancel = {
//                        screenScroll.resetTracking()
//                    },
//                    onDragEnd = {
//                        coroutineScope.launch {
//                            screenScroll.flingIfPossible()
//                        }
//                    }
//                )
//            },
//        itemProvider = itemProvider
//    ) { constraint ->
//
//        data class ItemData(val placeable: Placeable, val timetableItem: TimetableItemLayout)
//        timetableScreen.updateBounds(width = constraint.maxWidth, height = constraint.maxHeight)
//
//        val items = visibleItemLayouts.map { (index, timetableLayout) ->
//            ItemData(
//                placeable = measure(
//                    index,
//                    Constraints.fixed(
//                        width = timetableLayout.width,
//                        height = timetableLayout.height
//                    )
//                )[0],
//                timetableItem = timetableLayout
//            )
//        }
//        layout(constraint.maxWidth, constraint.maxHeight) {
//            items.forEach { (placable, timetableLayout) ->
//                placable.place(
//                    timetableLayout.left + timetableScreen.scrollState.scrollX.toInt(),
//                    timetableLayout.top + timetableScreen.scrollState.scrollY.toInt()
//                )
//            }
//        }
//    }
//}

@Preview(device = Devices.PIXEL_4)
@Composable
private fun TimetableWithHoursPreview(modifier: Modifier = Modifier) {
    val timetable = Timetable.fake()
    val timetableState = rememberTimetableState()
    val hoursList = listOf(
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
    Row(modifier = modifier) {
        Hours(
            modifier = modifier,
            hoursList = hoursList,
            timetableState = timetableState,
        ) { modifier, hour ->
            HoursItem(hour = hour, modifier = modifier)
        }
        Timetable(
            modifier = modifier,
            timetable = timetable,
            timetableState = timetableState
        ) { timetableItem, isFavorite ->
            TimetableItem(timetableItem, isFavorite)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Timetable(
    timetable: Timetable,
    timetableState: TimetableState,
    modifier: Modifier = Modifier,
    content: @Composable (TimetableItem, Boolean) -> Unit,
) {
    val itemProvider = itemProvider({ timetable.timetableItems.size }) { index ->
        val timetableItemWithFavorite = timetable.contents[index]
        content(timetableItemWithFavorite.timetableItem, timetableItemWithFavorite.isFavorited)
    }
    val density = LocalDensity.current
    val timetableLayout = remember(timetable) {
        TimetableLayout(timetable = timetable, density = density)
    }
    val coroutineScope = rememberCoroutineScope()
    val screenScroll = timetableState.screenScrollState
    val timetableScreen = remember(timetableLayout, density) {
        TimetableScreen(
            timetableLayout,
            screenScroll,
            density
        )
    }
    val visibleItemLayouts by remember(timetableScreen) { timetableScreen.visibleItemLayouts }
    val lineColor = MaterialTheme.colorScheme.surfaceVariant
    val linePxSize = with(LocalDensity.current) { timeTableLineStrokeSize.toPx() }
    LazyLayout(
        modifier = modifier
            .clipToBounds()
            .drawBehind {
                timetableScreen.timeHorizontalLines.value.forEach {
                    drawLine(
                        lineColor,
                        Offset(0F, it),
                        Offset(timetableScreen.width.toFloat(), it),
                        linePxSize
                    )
                }
                timetableScreen.roomVerticalLines.value.forEach {
                    drawLine(
                        lineColor,
                        Offset(it, 0f),
                        Offset(it, timetableScreen.height.toFloat()),
                        linePxSize
                    )
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        if (timetableScreen.enableHorizontalScroll(dragAmount.x)) {
                            if (change.positionChange() != Offset.Zero) change.consume()
                        }
                        coroutineScope.launch {
                            timetableScreen.scroll(
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

        data class ItemData(val placeable: Placeable, val timetableItem: TimetableItemLayout)
        timetableScreen.updateBounds(width = constraint.maxWidth, height = constraint.maxHeight)

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
                    timetableLayout.left + timetableScreen.scrollState.scrollX.toInt(),
                    timetableLayout.top + timetableScreen.scrollState.scrollY.toInt()
                )
            }
        }
    }
}

@Preview
@Composable
fun TimetablePreview() {
    val timetableState = rememberTimetableState()
    Timetable(
        modifier = Modifier.fillMaxSize(),
        timetable = Timetable.fake(),
        timetableState = timetableState
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

private data class HoursItemLayout(
    val density: Density,
    val minutePx: Int,
    val index: Int
) {
    val height = with(density) {
        112.dp.roundToPx()
    }
    val width = with(density) {
        64.dp.roundToPx()
    }
    val left = 0
    val top = index * height
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
        timeTableColumnWidth.roundToPx()
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
        (4.23).dp.roundToPx()
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

@Composable
fun rememberScreenScrollState(): ScreenScrollState = rememberSaveable(
    saver = ScreenScrollState.Saver
) {
    ScreenScrollState()
}

@Stable
class ScreenScrollState(
    initialScrollX: Float = 0f,
    initialScrollY: Float = 0f,
) {
    private val velocityTracker = VelocityTracker()
    private val _scrollX = Animatable(initialScrollX)
    private val _scrollY = Animatable(initialScrollY)

    val scrollX: Float
        get() = _scrollX.value
    val scrollY: Float
        get() = _scrollY.value

    val maxX: Float
        get() = _scrollX.lowerBound ?: 0f
    val maxY: Float
        get() = _scrollY.lowerBound ?: 0f

    suspend fun scroll(
        amount: Offset,
        timeMillis: Long,
        position: Offset,
    ) = coroutineScope {
        velocityTracker.addPosition(timeMillis = timeMillis, position = position)
        launch {
            _scrollX.snapTo(amount.x)
        }
        launch {
            _scrollY.snapTo(amount.y)
        }
    }

    suspend fun flingIfPossible() = coroutineScope {
        val velocity = velocityTracker.calculateVelocity()
        launch {
            _scrollX.animateDecay(
                velocity.x / 2f,
                exponentialDecay()
            )
        }
        launch {
            _scrollY.animateDecay(
                velocity.y / 2f,
                exponentialDecay()
            )
        }
    }

    fun updateBounds(maxX: Float, maxY: Float) {
        _scrollX.updateBounds(maxX, 0f)
        _scrollY.updateBounds(maxY, 0f)
    }

    fun resetTracking() {
        velocityTracker.resetTracking()
    }

    companion object {
        val Saver: Saver<ScreenScrollState, *> = listSaver(
            save = {
                listOf(
                    it.scrollX,
                    it.scrollY,
                )
            },
            restore = {
                ScreenScrollState(
                    initialScrollX = it[0],
                    initialScrollY = it[1],
                )
            }
        )
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

private class TimetableScreen(
    val timetableLayout: TimetableLayout,
    val scrollState: ScreenScrollState,
    private val density: Density,
) {
    var width = 0
        private set
    var height = 0
        private set

    val visibleItemLayouts: State<List<IndexedValue<TimetableItemLayout>>> =
        derivedStateOf {
            timetableLayout.visibleItemLayouts(
                width,
                height,
                scrollState.scrollX.toInt(),
                scrollState.scrollY.toInt()
            )
        }
    val timeHorizontalLines = derivedStateOf {
        val startTime = timetableLayout.dayStartTime ?: return@derivedStateOf listOf()
        val startMinute = startTime.toLocalDateTime((TimeZone.currentSystemDefault())).minute
        (0..10).map {
            val minuteOffSet = startMinute * timetableLayout.minutePx
            scrollState.scrollY + timetableLayout.minutePx * 60 * it - minuteOffSet
        }
    }
    val roomVerticalLines = derivedStateOf {
        val width = with(density) { timeTableColumnWidth.toPx() }
        val rooms = timetableLayout.rooms
        (0..rooms.lastIndex).map {
            scrollState.scrollX + width * it
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
        val nextPossibleX = calculatePossibleScrollX(dragAmount.x)
        val nextPossibleY = calculatePossibleScrollY(dragAmount.y)
        scrollState.scroll(
            Offset(nextPossibleX, nextPossibleY),
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
            maxX = if (width < timetableLayout.timetableWidth) {
                -(timetableLayout.timetableWidth - width).toFloat()
            } else {
                0f
            },
            maxY = if (height < timetableLayout.timetableHeight) {
                -(timetableLayout.timetableHeight - height).toFloat()
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

private val timeTableLineStrokeSize = 1.dp
private val timeTableColumnWidth = 192.dp

private val hoursWidth = 75.dp
private val hourItemWidth = 43.dp
private val hourItemHeight = 24.dp