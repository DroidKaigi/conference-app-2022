package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched2022.model.TimetableRoom

@Composable
fun RoomItem(
    room: TimetableRoom,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = room.name.currentLangTitle,
            color = Color.White
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Rooms(
    rooms: List<TimetableRoom>,
    timetableState: TimetableState,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier, TimetableRoom) -> Unit,
) {
    val itemProvider = itemProvider({ rooms.size }) { index ->
        content(modifier, rooms[index])
    }
    val density = timetableState.density
    val roomsLayout = remember(rooms) {
        RoomsLayout(
            rooms = rooms,
            density = density
        )
    }

    val roomsScreen = remember(roomsLayout, density) {
        RoomScreen(
            roomsLayout = roomsLayout,
            scrollState = timetableState.screenScrollState,
            density = density
        )
    }

    val visibleItemLayouts by remember(roomsScreen) { roomsScreen.visibleItemLayouts }

    val lineColor = MaterialTheme.colorScheme.surfaceVariant
    val linePxSize = with(timetableState.density) { timeTableLineStrokeSize.toPx() }

    LazyLayout(
        modifier = modifier
            .height(height = roomHeaderHeight)
            .clipToBounds()
            .drawBehind {
                roomsScreen.verticalLines.value.forEach {
                    drawLine(
                        lineColor,
                        Offset(it, 0f),
                        Offset(it, roomsScreen.height.toFloat()),
                        linePxSize
                    )
                }

                drawLine(
                    lineColor,
                    Offset(0f, roomsScreen.height.toFloat()),
                    Offset(roomsScreen.width.toFloat(), roomsScreen.height.toFloat()),
                    linePxSize
                )
            },
        itemProvider = itemProvider
    ) { constraints ->
        data class ItemData(val placeable: Placeable, val roomItem: RoomItemLayout)
        roomsScreen.updateBounds(width = constraints.maxWidth, height = constraints.maxHeight)

        val items = visibleItemLayouts.map { (index, roomLayout) ->
            ItemData(
                placeable = measure(
                    index = index,
                    constraints = Constraints.fixed(
                        width = roomLayout.width,
                        height = roomLayout.height
                    )
                )[0],
                roomItem = roomLayout
            )
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            items.forEach { (placeable, roomLayout) ->
                placeable.place(
                    roomLayout.left + roomsScreen.scrollState.scrollX.toInt(),
                    roomLayout.top
                )
            }
        }
    }
}

private data class RoomsLayout(
    val rooms: List<TimetableRoom>,
    val density: Density
) {
    var roomsWidth = 0
    var roomsHeight = 0
    val roomsLayouts = rooms.mapIndexed { index, _ ->
        val itemLayout = RoomItemLayout(
            index = index,
            density = density
        )

        roomsHeight = maxOf(roomsHeight, itemLayout.height)
        roomsWidth = maxOf(roomsWidth, itemLayout.right)
        itemLayout
    }

    fun visibleItemLayouts(
        screenWidth: Int,
        scrollX: Int
    ): List<IndexedValue<RoomItemLayout>> {
        return roomsLayouts.withIndex().filter { (_, layout) ->
            layout.isVisible(screenWidth, scrollX)
        }
    }
}

private data class RoomItemLayout(
    val density: Density,
    val index: Int,
) {
    val width = with(density) { roomHeaderWidth.roundToPx() }
    val height = with(density) { roomHeaderHeight.roundToPx() }
    val left = index * width
    val top = 0
    val right = left + width
    val bottom = top + height

    fun isVisible(
        screenWidth: Int,
        scrollX: Int
    ): Boolean {
        val screenStart = -scrollX
        val screenEnd = -scrollX + screenWidth
        val xInside = left in screenStart..screenEnd || right in screenStart..screenEnd
        return xInside
    }
}

private class RoomScreen(
    val roomsLayout: RoomsLayout,
    val scrollState: ScreenScrollState,
    density: Density,
) {
    var width = 0
        private set
    var height = 0
        private set

    val visibleItemLayouts: State<List<IndexedValue<RoomItemLayout>>> =
        derivedStateOf {
            roomsLayout.visibleItemLayouts(
                width,
                scrollState.scrollX.toInt()
            )
        }

    val verticalLines = derivedStateOf {
        val width = with(density) { roomHeaderWidth.toPx() }
        val rooms = roomsLayout.rooms
        (0..rooms.lastIndex).map {
            scrollState.scrollX + width * it
        }
    }

    override fun toString(): String {
        return "RoomScreen(" +
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

private val timeTableLineStrokeSize = 1.dp
private val roomHeaderWidth = 192.dp
private val roomHeaderHeight = 48.dp