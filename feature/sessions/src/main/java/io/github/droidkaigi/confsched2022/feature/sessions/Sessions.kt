package io.github.droidkaigi.confsched2022.feature.sessions

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.view.doOnPreDraw
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.model.DroidKaigi2022Day
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.TimetableItemWithFavorite
import io.github.droidkaigi.confsched2022.model.fake
import io.github.droidkaigi.confsched2022.model.orEmptyContents
import io.github.droidkaigi.confsched2022.ui.UiLoadState
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Error
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Loading
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Success
import io.github.droidkaigi.confsched2022.ui.pagerTabIndicatorOffset
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import io.github.droidkaigi.confsched2022.core.designsystem.R as CoreR

@Composable
fun SessionsScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SessionsViewModel = hiltViewModel(),
    showNavigationIcon: Boolean = true,
    onNavigationIconClick: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
    onTimetableClick: (TimetableItemId) -> Unit = {},
) {
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
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    onTimetableClick: (timetableItemId: TimetableItemId) -> Unit,
    onFavoriteClick: (TimetableItemId, Boolean) -> Unit,
    onSearchClick: () -> Unit,
    onToggleTimetableClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scheduleState = uiModel.state
    val isTimetable = uiModel.isTimetable
    val pagerState = rememberPagerState()
    val sessionsListListStates = DroidKaigi2022Day.values().map { rememberLazyListState() }.toList()
    val timetableListStates = DroidKaigi2022Day.values().map {
        rememberTimetableState(screenScaleState = rememberScreenScaleState())
    }.toList()
    val pagerContentsScrollState = rememberPagerContentsScrollState(
        isTimetable,
        pagerState,
        timetableListStates,
        sessionsListListStates
    )
    KaigiScaffold(
        modifier = modifier,
        topBar = {
            SessionsTopBar(
                pagerContentsScrollState,
                isTimetable,
                scheduleState,
                showNavigationIcon,
                onNavigationIconClick,
                onSearchClick,
                onToggleTimetableClick
            )
        }
    ) { innerPadding ->
        Column {
            when (scheduleState) {
                is Error -> {
                    scheduleState.value?.printStackTrace()
                    TODO()
                }
                Loading -> Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
                is Success -> {
                    val schedule = scheduleState.value
                    val days = schedule.days
                    if (isTimetable) {
                        Timetable(
                            pagerState = pagerState,
                            schedule = schedule,
                            timetableListStates = pagerContentsScrollState.timetableStates,
                            days = days,
                            onTimetableClick = onTimetableClick,
                            contentPadding = innerPadding,
                        )
                    } else {
                        SessionsList(
                            pagerState = pagerState,
                            sessionsListListStates = pagerContentsScrollState.sessionsListStates,
                            schedule = schedule,
                            days = days,
                            onTimetableClick = onTimetableClick,
                            onFavoriteClick = onFavoriteClick,
                            contentPadding = innerPadding,
                        )
                    }
                }
            }
            if (scheduleState !is Loading) {
                // Workaround to call Activity.reportFullyDrawn from Jetpack Compose.
                // ref. https://github.com/android/nowinandroid/blob/c67812563be5a710ea8a57b0580f6436599df8a5/feature-foryou/src/main/java/com/google/samples/apps/nowinandroid/feature/foryou/ForYouScreen.kt#L153
                val localView = LocalView.current
                LaunchedEffect(Unit) {
                    val activity = localView.context as? Activity ?: return@LaunchedEffect
                    localView.doOnPreDraw { activity.reportFullyDrawn() }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Timetable(
    pagerState: PagerState,
    timetableListStates: List<TimetableState>,
    schedule: DroidKaigiSchedule,
    days: Array<DroidKaigi2022Day>,
    onTimetableClick: (TimetableItemId) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    HorizontalPager(
        count = days.size,
        modifier = modifier,
        state = pagerState
    ) { dayIndex ->
        val day = days[dayIndex]
        val timetable = schedule.dayToTimetable[day].orEmptyContents()
        val timetableState = timetableListStates[dayIndex]
        val coroutineScope = rememberCoroutineScope()
        val layoutDirection = LocalLayoutDirection.current

        Row(
            modifier = Modifier.padding(
                top = contentPadding.calculateTopPadding(),
                start = contentPadding.calculateStartPadding(layoutDirection),
                end = contentPadding.calculateEndPadding(layoutDirection),
            )
        ) {
            Hours(
                modifier = Modifier.transformable(
                    rememberTransformableStateForScreenScale(timetableState.screenScaleState),
                ),
                timetableState = timetableState,
            ) { hour ->
                HoursItem(hour = hour)
            }

            Column(modifier = Modifier.weight(1f)) {
                Rooms(
                    rooms = timetable.rooms,
                    timetableState = timetableState
                ) { room ->
                    RoomItem(room = room)
                }

                Timetable(
                    timetable = timetable,
                    timetableState = timetableState,
                    coroutineScope = coroutineScope,
                    contentPadding = PaddingValues(
                        bottom = contentPadding.calculateBottomPadding(),
                    )
                ) { timetableItem, isFavorited ->
                    TimetableItem(
                        timetableItem = timetableItem,
                        isFavorited = isFavorited,
                        verticalScale = timetableState.screenScaleState.verticalScale,
                        modifier = Modifier
                            .padding(horizontal = 2.dp)
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
    schedule: DroidKaigiSchedule,
    days: Array<DroidKaigi2022Day>,
    onTimetableClick: (timetableItemId: TimetableItemId) -> Unit,
    onFavoriteClick: (TimetableItemId, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    HorizontalPager(
        modifier = modifier,
        count = days.size,
        state = pagerState
    ) { dayIndex ->
        val day = days[dayIndex]
        val timetable = schedule.dayToTimetable[day].orEmptyContents()
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
            sessionsListListState = sessionsListListStates[dayIndex],
            contentPadding = contentPadding
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
                                Text(
                                    text = it.startAt,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Box(
                                    modifier = Modifier
                                        .size(1.dp, 2.dp)
                                        .background(MaterialTheme.colorScheme.onBackground)
                                ) { }
                                Text(
                                    text = it.endAt,
                                    style = MaterialTheme.typography.titleMedium
                                )
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
    pagerContentsScrollState: PagerContentsScrollState,
    isTimetable: Boolean,
    scheduleState: UiLoadState<DroidKaigiSchedule>,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    onSearchClick: () -> Unit,
    onToggleTimetableClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = pagerContentsScrollState.pagerState
    Column(
        modifier = modifier
    ) {
        KaigiTopAppBar(
            showNavigationIcon = showNavigationIcon,
            onNavigationIconClick = onNavigationIconClick,
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

        if (scheduleState is Success) {
            val days = scheduleState.value.days
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme
                            .surfaceColorAtElevation(2.dp)
                    )
                    .padding(16.dp)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
                    ),
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
                        expanded = pagerContentsScrollState.isScrollTop,
                        onTabClicked = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(it)
                                if (selected) {
                                    pagerContentsScrollState.scrollToSessionsListItem(0)
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
                state = Success(DroidKaigiSchedule.fake()),
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
                state = Success(DroidKaigiSchedule.fake()),
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
                state = Loading,
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun rememberPagerContentsScrollState(
    isTimetable: Boolean,
    pagerState: PagerState,
    timetableStates: List<TimetableState>,
    sessionsListListStates: List<LazyListState>
): PagerContentsScrollState =
    remember(isTimetable, pagerState, timetableStates, sessionsListListStates) {
        PagerContentsScrollState(isTimetable, pagerState, timetableStates, sessionsListListStates)
    }

@OptIn(ExperimentalPagerApi::class)
@Stable
class PagerContentsScrollState(
    private val isTimetable: Boolean,
    val pagerState: PagerState,
    val timetableStates: List<TimetableState>,
    val sessionsListStates: List<LazyListState>
) {
    val isScrollTop by derivedStateOf {
        if (isTimetable) {
            timetableStates[pagerState.currentPage].screenScrollState.scrollY > -1F
        } else {
            sessionsListStates[pagerState.currentPage].let {
                it.firstVisibleItemIndex == 0 && it.firstVisibleItemScrollOffset == 0
            }
        }
    }

    suspend fun scrollToSessionsListItem(index: Int) = coroutineScope {
        launch {
            sessionsListStates[pagerState.currentPage].scrollToItem(index)
        }
    }
}
