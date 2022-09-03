package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
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
        val timetableItemWithFavorite = timetable.contents[index]
        content(timetableItemWithFavorite.timetableItem, timetableItemWithFavorite.isFavorited)
    }
    val density = LocalDensity.current
    val timetableLayout = remember(timetable) {
        TimetableLayout(timetable = timetable, density = density)
    }
    val coroutineScope = rememberCoroutineScope()
    val screenScroll = rememberScreenScrollState()
    val screen = remember(timetableLayout, density) {
        Screen(
            timetableLayout,
            screenScroll,
            density
        )
    }
    val visibleItemLayouts by remember(screen) { screen.visibleItemLayouts }
    val lineColor = MaterialTheme.colorScheme.surfaceVariant
    val linePxSize = with(LocalDensity.current) { timeTableLineStrokeSize.toPx() }
    LazyLayout(
        modifier = modifier
            .clipToBounds()
            .drawBehind {
                screen.timeHorizontalLines.value.forEach {
                    drawLine(
                        lineColor,
                        Offset(0F, it),
                        Offset(screen.width.toFloat(), it),
                        linePxSize
                    )
                }
                screen.roomVerticalLines.value.forEach {
                    drawLine(
                        lineColor,
                        Offset(it, 0f),
                        Offset(it, screen.height.toFloat()),
                        linePxSize
                    )
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        // FIXME: Pager swipe is not possible because scrolling is not transmitted to the pager side
                        if (change.positionChange() != Offset.Zero) change.consume()
                        coroutineScope.launch {
                            screen.scroll(
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
        screen.updateBounds(width = constraint.maxWidth, height = constraint.maxHeight)

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
                    timetableLayout.left + screen.screenScrollState.scrollX.toInt(),
                    timetableLayout.top + screen.screenScrollState.scrollY.toInt()
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
        TimetableItem(timetableItem, isFavorite, { _, _ -> })
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
        _scrollY.updateBounds(-maxY, 0f)
        _scrollX.updateBounds(-maxX, 0f)
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

private class Screen(
    val timetableLayout: TimetableLayout,
    val screenScrollState: ScreenScrollState,
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
                screenScrollState.scrollX.toInt(),
                screenScrollState.scrollY.toInt()
            )
        }
    val timeHorizontalLines = derivedStateOf {
        val startTime = timetableLayout.dayStartTime ?: return@derivedStateOf listOf()
        val startMinute = startTime.toLocalDateTime((TimeZone.currentSystemDefault())).minute
        (0..10).map {
            val minuteOffSet = startMinute * timetableLayout.minutePx
            screenScrollState.scrollY + timetableLayout.minutePx * 60 * it - minuteOffSet
        }
    }
    val roomVerticalLines = derivedStateOf {
        val width = with(density) { timeTableColumnWidth.toPx() }
        val rooms = timetableLayout.rooms
        (0..rooms.lastIndex).map {
            screenScrollState.scrollX + width * it
        }
    }

    override fun toString(): String {
        return "Screen(" +
            "width=$width, " +
            "height=$height, " +
            "scroll=$screenScrollState, " +
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
        screenScrollState.scroll(
            Offset(nextPossibleX, nextPossibleY),
            timeMillis,
            position
        )
    }

    fun updateBounds(width: Int, height: Int) {
        this.width = width
        this.height = height
        screenScrollState.updateBounds(
            maxX = if (width < timetableLayout.timetableWidth) {
                (timetableLayout.timetableWidth - width).toFloat()
            } else {
                0f
            },
            maxY = if (height < timetableLayout.timetableHeight) {
                (timetableLayout.timetableHeight - height).toFloat()
            } else {
                0f
            }
        )
    }

    private fun calculatePossibleScrollX(scrollX: Float): Float {
        val currentValue = screenScrollState.scrollX
        val nextValue = currentValue + scrollX
        val maxScroll = if (width < timetableLayout.timetableWidth) {
            -(timetableLayout.timetableWidth - width)
        } else {
            0
        }
        return maxOf(minOf(nextValue.toInt(), 0), maxScroll).toFloat()
    }

    private fun calculatePossibleScrollY(scrollY: Float): Float {
        val currentValue = screenScrollState.scrollY
        val nextValue = currentValue + scrollY
        val maxScroll = if (height < timetableLayout.timetableHeight) {
            -(timetableLayout.timetableHeight - height)
        } else {
            0
        }
        return maxOf(minOf(nextValue.toInt(), 0), maxScroll).toFloat()
    }
}

private val timeTableLineStrokeSize = 1.dp
private val timeTableColumnWidth = 192.dp
