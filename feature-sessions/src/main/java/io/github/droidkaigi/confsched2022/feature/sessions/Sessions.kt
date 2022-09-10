package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.feature.sessions.SessionsUiModel.ScheduleState
import io.github.droidkaigi.confsched2022.feature.sessions.SessionsUiModel.ScheduleState.Loaded
import io.github.droidkaigi.confsched2022.model.DroidKaigi2022Day
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.TimetableItemWithFavorite
import io.github.droidkaigi.confsched2022.model.fake
import io.github.droidkaigi.confsched2022.model.orEmptyContents
import io.github.droidkaigi.confsched2022.ui.pagerTabIndicatorOffset
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import io.github.droidkaigi.confsched2022.core.designsystem.R as CoreR

@Composable
fun SessionsScreenRoot(
    modifier: Modifier = Modifier,
    showNavigationIcon: Boolean = true,
    onNavigationIconClick: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
    onTimetableClick: (TimetableItemId) -> Unit = {},
) {
    val viewModel = hiltViewModel<SessionsViewModel>()
    val state: SessionsUiModel by viewModel.uiModel

    Sessions(
        uiModel = state,
        modifier = modifier,
        onTimetableClick = { onTimetableClick(it) },
        onFavoriteClick = { timetableItemId, isFavorite ->
            viewModel.onFavoriteToggle(timetableItemId, isFavorite)
        },
        showNavigationIcon = showNavigationIcon,
        onNavigationIconClick = onNavigationIconClick,
        onSearchClick = onSearchClicked,
        onToggleTimetableClick = { isTimetable ->
            viewModel.onTimetableModeToggle(isTimetable)
        },
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Sessions(
    uiModel: SessionsUiModel,
    modifier: Modifier = Modifier,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    onTimetableClick: (timetableItemId: TimetableItemId) -> Unit,
    onFavoriteClick: (TimetableItemId, Boolean) -> Unit,
    onSearchClick: () -> Unit,
    onToggleTimetableClick: (Boolean) -> Unit,
) {
    val scheduleState = uiModel.scheduleState
    val isTimetable = uiModel.isTimetable
    val pagerState = rememberPagerState()
    val sessionsListListStates = DroidKaigi2022Day.values().map { rememberLazyListState() }.toList()
    KaigiScaffold(
        modifier = modifier,
        topBar = {
            SessionsTopBar(
                pagerState,
                isTimetable,
                if (isTimetable) null else sessionsListListStates,
                scheduleState,
                showNavigationIcon,
                onNavigationIconClick,
                onSearchClick,
                onToggleTimetableClick
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(top = 4.dp)
        ) {
            if (scheduleState !is Loaded) {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val days = scheduleState.schedule.days
                if (isTimetable) {
                    Timetable(
                        pagerState = pagerState,
                        scheduleState = scheduleState,
                        days = days,
                        onTimetableClick = onTimetableClick
                    )
                } else {
                    SessionsList(
                        pagerState = pagerState,
                        sessionsListListStates = sessionsListListStates,
                        scheduleState = scheduleState,
                        days = days,
                        onTimetableClick = onTimetableClick,
                        onFavoriteClick = onFavoriteClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Timetable(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    scheduleState: Loaded,
    days: Array<DroidKaigi2022Day>,
    onTimetableClick: (TimetableItemId) -> Unit,
) {
    val screenScaleState = rememberScreenScaleState()
    HorizontalPager(
        count = days.size,
        modifier = modifier,
        state = pagerState
    ) { dayIndex ->
        val day = days[dayIndex]
        val timetable = scheduleState.schedule.dayToTimetable[day].orEmptyContents()
        val timetableState = rememberTimetableState(screenScaleState = screenScaleState)
        val coroutineScope = rememberCoroutineScope()

        Row {
            Hours(
                modifier = modifier.transformable(
                    rememberTransformableStateForScreenScale(timetableState.screenScaleState),
                ),
                timetableState = timetableState,
            ) { modifier, hour ->
                HoursItem(hour = hour, modifier = modifier)
            }

            Column(modifier = Modifier.weight(1f)) {
                Rooms(
                    rooms = timetable.rooms,
                    timetableState = timetableState
                ) { modifier, room ->
                    RoomItem(modifier = modifier, room = room)
                }

                Timetable(
                    timetable = timetable,
                    timetableState = timetableState,
                    coroutineScope,
                ) { timetableItem, isFavorited ->
                    TimetableItem(
                        timetableItem = timetableItem,
                        isFavorited = isFavorited,
                        verticalScale = timetableState.screenScaleState.verticalScale,
                        modifier = Modifier
                            .clickable(
                                onClick = { onTimetableClick(timetableItem.id) }
                            ),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SessionsList(
    pagerState: PagerState,
    sessionsListListStates: List<LazyListState>,
    scheduleState: Loaded,
    days: Array<DroidKaigi2022Day>,
    onTimetableClick: (timetableItemId: TimetableItemId) -> Unit,
    onFavoriteClick: (TimetableItemId, Boolean) -> Unit,
) {
    HorizontalPager(
        count = days.size,
        state = pagerState
    ) { dayIndex ->
        val day = days[dayIndex]
        val timetable = scheduleState.schedule.dayToTimetable[day].orEmptyContents()
        val timeHeaderAndTimetableItems = remember(timetable) {
            var currentStartTime = ""
            val list = mutableListOf<Pair<DurationTime?, TimetableItemWithFavorite>>()
            timetable.contents.forEachIndexed { index, timetableItemWithFavorite ->
                val startLocalDateTime = timetableItemWithFavorite.timetableItem.startsAt
                    .toLocalDateTime(TimeZone.of("UTC+9"))
                val endLocalDateTime = timetableItemWithFavorite.timetableItem.endsAt
                    .toLocalDateTime(TimeZone.of("UTC+9"))
                val startTime = startLocalDateTime.time.toString()
                val endTime = endLocalDateTime.time.toString()
                if (index > 0 && startTime == currentStartTime) {
                    list.add(Pair(null, timetableItemWithFavorite))
                } else {
                    currentStartTime = startTime
                    list.add(Pair(DurationTime(startTime, endTime), timetableItemWithFavorite))
                }
            }
            list.toList()
        }
        SessionList(
            timetable = timeHeaderAndTimetableItems,
            sessionsListListState = sessionsListListStates[dayIndex]
        ) { (timeHeader, timetableItemWithFavorite) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTimetableClick(timetableItemWithFavorite.timetableItem.id) }
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.width(85.dp),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            timeHeader?.let {
                                Text(text = it.startAt)
                                Box(
                                    modifier = Modifier
                                        .size(1.dp, 2.dp)
                                        .background(MaterialTheme.colorScheme.onBackground)
                                ) { }
                                Text(text = it.endAt)
                            }
                        }
                    }
                    SessionListItem(
                        timetableItem = timetableItemWithFavorite.timetableItem,
                        isFavorited = timetableItemWithFavorite.isFavorited,
                        onFavoriteClick = onFavoriteClick
                    )
                }
            }
        }
    }
}

data class DurationTime(val startAt: String, val endAt: String)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SessionsTopBar(
    pagerState: PagerState,
    isTimetable: Boolean,
    sessionsListListStates: List<LazyListState>?,
    scheduleState: ScheduleState,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    onSearchClick: () -> Unit,
    onToggleTimetableClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        KaigiTopAppBar(
            showNavigationIcon = showNavigationIcon,
            onNavigationIconClick = onNavigationIconClick,
            elevation = 2.dp,
            title = {
                Image(
                    modifier = Modifier.size(30.dp),
                    imageVector = ImageVector.vectorResource(id = CoreR.drawable.ic_app),
                    contentDescription = "logo in toolbar"
                )
            },
            trailingIcons = {
                IconButton(
                    onClick = onSearchClick,
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_search
                        ),
                        contentDescription = "Search icon"
                    )
                }
                IconButton(
                    onClick = { onToggleTimetableClick(isTimetable) }
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_today
                        ),
                        contentDescription = "Toggle timetable icon"
                    )
                }
            }
        )
        (scheduleState as? Loaded)?.schedule?.days?.let { days ->
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme
                            .surfaceColorAtElevation(2.dp)
                    )
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme
                    .surfaceColorAtElevation(2.dp),
                indicator = {
                    TabIndicator(
                        modifier = Modifier
                            .pagerTabIndicatorOffset(pagerState, it)
                            .zIndex(-1f),
                    )
                },
                divider = {},
            ) {
                val coroutineScope = rememberCoroutineScope()
                days.forEachIndexed { index, day ->
                    val selected = pagerState.currentPage == index
                    SessionDayTab(
                        index = index,
                        day = day,
                        selected = selected,
                        onTabClicked = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(it)
                                if (selected) {
                                    sessionsListListStates?.let {
                                        sessionsListListStates[index].scrollToItem(index = 0)
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TabIndicator(
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(percent = 50),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(end = 8.dp)
    ) {}
}

@Preview(showBackground = true)
@Composable
fun SessionsTimetablePreview() {
    KaigiTheme {
        Sessions(
            uiModel = SessionsUiModel(
                scheduleState = Loaded(DroidKaigiSchedule.fake()),
                isFilterOn = false,
                isTimetable = true
            ),
            showNavigationIcon = true,
            onNavigationIconClick = {},
            onTimetableClick = {},
            onFavoriteClick = { _, _ -> },
            onSearchClick = {},
            onToggleTimetableClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SessionsSessionListPreview() {
    KaigiTheme {
        Sessions(
            uiModel = SessionsUiModel(
                scheduleState = Loaded(DroidKaigiSchedule.fake()),
                isFilterOn = false,
                isTimetable = false
            ),
            showNavigationIcon = true,
            onNavigationIconClick = {},
            onTimetableClick = {},
            onFavoriteClick = { _, _ -> },
            onSearchClick = {},
            onToggleTimetableClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SessionsLoadingPreview() {
    KaigiTheme {
        Sessions(
            uiModel = SessionsUiModel(
                scheduleState = ScheduleState.Loading,
                isFilterOn = false,
                isTimetable = true
            ),
            onNavigationIconClick = {},
            onTimetableClick = {},
            onFavoriteClick = { _, _ -> },
            onSearchClick = {},
            onToggleTimetableClick = {},
            showNavigationIcon = true
        )
    }
}
