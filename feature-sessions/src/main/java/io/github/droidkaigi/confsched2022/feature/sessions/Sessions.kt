package io.github.droidkaigi.confsched2022.feature.sessions

import android.content.res.Resources.Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.feature.sessions.SessionsUiModel.ScheduleState.Loaded
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.orEmptyContents
import io.github.droidkaigi.confsched2022.ui.ifTrue
import io.github.droidkaigi.confsched2022.ui.pagerTabIndicatorOffset

@Composable
fun SessionsScreenRoot(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {}
) {
    val viewModel = hiltViewModel<SessionsViewModel>()
    val state: SessionsUiModel by viewModel.uiModel

    var tabState by remember { mutableStateOf(0) }

    Sessions(
        uiModel = state,
        modifier = modifier,
        selectedTab = tabState,
        onTimetableClick = {},
        onTabClicked = { index -> tabState = index },
        onToggleFilter = { viewModel.onToggleFilter() },
        onFavoriteClick = { timetableItemId, isFavorite ->
            viewModel.onFavoriteToggle(timetableItemId, isFavorite)
        },
        onNavigationIconClick = onNavigationIconClick,
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Sessions(
    uiModel: SessionsUiModel,
    modifier: Modifier = Modifier,
    selectedTab: Int,
    onNavigationIconClick: () -> Unit,
    onTimetableClick: (timetableItemId: TimetableItemId) -> Unit,
    onTabClicked: (index: Int) -> Unit,
    onToggleFilter: () -> Unit,
    onFavoriteClick: (TimetableItemId, Boolean) -> Unit
) {
    KaigiScaffold(onNavigationIconClick = onNavigationIconClick) {
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
                selectedTabIndex = selectedTab,
                indicator = {},
                divider = {},
            ) {
                days.forEachIndexed { index, day ->
                    val isSelected = selectedTab == index
                    Tab(
                        selected = isSelected,
                        onClick = { onTabClicked(selectedTab) },
                        modifier = Modifier.height(56.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(32.dp))
                                .ifTrue(isSelected) {
                                    Modifier.background(color = MaterialTheme.colorScheme.secondaryContainer)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = day.name,
                                    style = androidx.compose.ui.text.TextStyle(
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight(500)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = "${5 + index}",
                                    style = androidx.compose.ui.text.TextStyle(
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight(500)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
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
                        modifier = Modifier.clickable(onClick = { onTimetableClick(timetableItem.id) }),
                        onFavoriteClick = onFavoriteClick
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SessionsPreview() {
    KaigiTheme {
        SessionsScreenRoot()
    }
}
