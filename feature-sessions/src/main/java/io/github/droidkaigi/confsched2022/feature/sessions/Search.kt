package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.feature.sessions.SessionsUiModel.ScheduleState.Loaded
import io.github.droidkaigi.confsched2022.feature.sessions.SessionsUiModel.ScheduleState.Loading
import io.github.droidkaigi.confsched2022.model.DroidKaigi2022Day
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.TimetableItemWithFavorite

@Composable
fun SearchRoot() {
    val viewModel = hiltViewModel<SessionsViewModel>()
    val state: SessionsUiModel by viewModel.uiModel
    SearchScreen(
        uiModel = state
    )
}

@Composable
fun SearchScreen(
    uiModel: SessionsUiModel
) {
    KaigiScaffold(
        topBar = {

        },
        content = {
            Column {
                SearchTextField()
                when (uiModel.scheduleState) {
                    is Loaded -> {
                        SearchedItemListField(uiModel.scheduleState.schedule)
                    }
                    is Loading -> {
                        FullScreenLoading()
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTextField() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            value = "",
            modifier = Modifier
                .fillMaxWidth(fraction = 0.9F)
                .background(color = MaterialTheme.colorScheme.surfaceVariant),
            placeholder = { Text("Search Session") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "search_icon"
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "search_word_delete_icon"
                )
            },
            onValueChange = {},
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SearchedItemListField(schedule: DroidKaigiSchedule) {
    LazyColumn {
        schedule.dayToTimetable.forEach { (dayToTimeTable, timeTable) ->
            stickyHeader {
                SearchedHeader(day = dayToTimeTable)
            }
            items(timeTable.contents) {
                SearchedItem(it)
            }
        }
    }
}

@Composable
private fun SearchedHeader(day: DroidKaigi2022Day) {
    Text(text = day.name)
}

@Composable
private fun SearchedItem(timetableItemWithFavorite: TimetableItemWithFavorite) {
    Box {
        Column {
            Row {
                timetableItemWithFavorite.timetableItem
                Text(timetableItemWithFavorite.timetableItem.title.currentLangTitle)
                if (timetableItemWithFavorite.isFavorited) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark_filled),
                        contentDescription = "book_mark_icon"
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark),
                        contentDescription = "book_mark_icon"
                    )
                }
            }
            Text(timetableItemWithFavorite.timetableItem.startsAt.toString())
        }
    }
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
private fun SearchTextFieldPreview() {
    SearchTextField()
}