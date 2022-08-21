package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import io.github.droidkaigi.confsched2022.designsystem.theme.DroidKaigiTheme
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.orEmptyContents
import io.github.droidkaigi.confsched2022.feature.sessions.SessionsUiModel.ScheduleState.Loaded
import io.github.droidkaigi.confsched2022.pagerTabIndicatorOffset

@Composable
fun SessionsScreenRoot(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<SessionsViewModel>()
    val state: SessionsUiModel by viewModel.state

    var tabState by remember { mutableStateOf(0) }

    Sessions(
        sessionsUiModel = state,
        modifier = modifier,
        selectedTab = tabState,
        onTimetableClick = {},
        onTabClicked = { index -> tabState = index },
        onToggleFilter = { viewModel.onToggleFilter() }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Sessions(
    sessionsUiModel: SessionsUiModel,
    modifier: Modifier = Modifier,
    selectedTab: Int,
    onTimetableClick: (timetableItemId: TimetableItemId) -> Unit,
    onTabClicked: (index: Int) -> Unit,
    onToggleFilter: () -> Unit
) {
    val scheduleState = sessionsUiModel.scheduleState
    if (scheduleState !is Loaded) {
        CircularProgressIndicator()
        return
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
            selectedTabIndex = selectedTab,
            indicator = {
                TabRowDefaults.Indicator(
                    modifier = Modifier.pagerTabIndicatorOffset(pagerState, it),
                )
            }
        ) {
            days.forEachIndexed { index, day ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { onTabClicked(selectedTab) },
                    text = { Text(text = day.name, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                )
            }
        }
        Text(
            text = "Filter is ${if (sessionsUiModel.isFilterOn) "ON" else "OFF"}",
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
                    modifier = Modifier.clickable(onClick = { onTimetableClick(timetableItem.id) }),
                    timetableItem = timetableItem,
                    isFavorited = isFavorited
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SessionsPreview() {
    DroidKaigiTheme {
        SessionsScreenRoot()
    }
}
