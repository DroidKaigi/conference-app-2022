package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.feature.sessions.SessionsUiModel.ScheduleState.Loaded
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.orEmptyContents
import io.github.droidkaigi.confsched2022.ui.pagerTabIndicatorOffset
import kotlinx.coroutines.launch

@Composable
fun SessionsScreenRoot(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
    onTodayClicked: () -> Unit = {},
) {
    val viewModel = hiltViewModel<SessionsViewModel>()
    val state: SessionsUiModel by viewModel.uiModel

    Sessions(
        uiModel = state,
        modifier = modifier,
        onTimetableClick = {},
        onToggleFilter = { viewModel.onToggleFilter() },
        onFavoriteClick = { timetableItemId, isFavorite ->
            viewModel.onFavoriteToggle(timetableItemId, isFavorite)
        },
        onNavigationIconClick = onNavigationIconClick,
        onSearchClicked = onSearchClicked,
        onTodayClicked = onTodayClicked,
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Sessions(
    uiModel: SessionsUiModel,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit,
    onTimetableClick: (timetableItemId: TimetableItemId) -> Unit,
    onToggleFilter: () -> Unit,
    onFavoriteClick: (TimetableItemId, Boolean) -> Unit,
    onSearchClicked: () -> Unit,
    onTodayClicked: () -> Unit
) {
    KaigiScaffold(
        onNavigationIconClick = onNavigationIconClick,
        onSearchClick = onSearchClicked,
        onTodayClick = onTodayClicked
    ) {
        val scheduleState = uiModel.scheduleState
        if (scheduleState !is Loaded) {
            CircularProgressIndicator()
            return@KaigiScaffold
        }
        val days = scheduleState.schedule.days
        Column(
            modifier = modifier
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                )
        ) {
            val pagerState = rememberPagerState()
            TabRow(
                selectedTabIndex = pagerState.currentPage,
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
                    SessionDayTab(
                        index = index,
                        day = day,
                        selected = pagerState.currentPage == index,
                        onTabClicked = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(it)
                            }
                        }
                    )
                }
            }
            Text(
                text = "Filter is ${if (uiModel.isFilterOn) "ON" else "OFF"}",
                modifier = Modifier.clickable(onClick = onToggleFilter)
            )
            HorizontalPager(
                count = days.size,
                state = pagerState
            ) { dayIndex ->
                val day = days[dayIndex]
                val timetable = scheduleState.schedule.dayToTimetable[day].orEmptyContents()
                Timetable(timetable) { timetableItem, isFavorited ->
                    TimetableItem(
                        timetableItem = timetableItem,
                        isFavorited = isFavorited,
                        modifier = Modifier
                            .clickable(
                                onClick = { onTimetableClick(timetableItem.id) }
                            ),
                        onFavoriteClick = onFavoriteClick
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
    ) {}
}

@Preview(showBackground = true)
@Composable
fun SessionsPreview() {
    KaigiTheme {
        SessionsScreenRoot()
    }
}
