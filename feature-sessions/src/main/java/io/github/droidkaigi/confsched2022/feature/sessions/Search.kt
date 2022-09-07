package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiScaffold
import io.github.droidkaigi.confsched2022.feature.sessions.SessionsUiModel.ScheduleState.Loaded
import io.github.droidkaigi.confsched2022.feature.sessions.SessionsUiModel.ScheduleState.Loading
import io.github.droidkaigi.confsched2022.model.DroidKaigi2022Day
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.Filters
import io.github.droidkaigi.confsched2022.model.TimetableItem.Session
import io.github.droidkaigi.confsched2022.model.TimetableItemWithFavorite
import io.github.droidkaigi.confsched2022.model.fake

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
            val sessions = timeTable.filtered(Filters(filterSession = true)).contents
            items(sessions) {
                SearchedItem(it)
            }
        }
    }
}

@Composable
private fun SearchedHeader(day: DroidKaigi2022Day) {
    Text(text = day.name)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchedItem(timetableItemWithFavorite: TimetableItemWithFavorite) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .heightIn(min = 120.dp, max = 140.dp)
    ) {
        Column(modifier = Modifier.padding(start = 15.dp, end = 10.dp, top = 15.dp)) {
            Row {
                val timeTable = timetableItemWithFavorite.timetableItem
                if (timeTable is Session) {
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .padding(top = 10.dp)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.TopCenter,
                    ) {
                        AsyncImage(
                            model = timeTable.speakers.firstOrNull()?.iconUrl,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape),
                            contentDescription = "Speaker Icon",
                        )
                    }

                    val bookMarkIconResource = if (timetableItemWithFavorite.isFavorited) {
                        R.drawable.ic_bookmark_filled
                    } else {
                        R.drawable.ic_bookmark
                    }

                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(timeTable.title.currentLangTitle)
                        Text(timeTable.startsTimeString)
                        Row(
                            modifier = Modifier
                                .height(40.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(5f)
                                    .height(40.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Badge {
                                    Text(text = timeTable.category.title.currentLangTitle)
                                }
                            }
                            Icon(
                                painter = painterResource(id = bookMarkIconResource),
                                contentDescription = "book_mark_icon",
                                modifier = Modifier
                                    .size(30.dp)
                                    .weight(1f)
                            )
                        }
                    }
                }
            }
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

@Preview(showBackground = true)
@Composable
private fun SearchedItemPreview() {
    val fakeSession =
        io.github.droidkaigi.confsched2022.model.TimetableItem.Session.Companion.fake()
    val favorite = true
    val timeTableWithFavorite = TimetableItemWithFavorite(fakeSession, favorite)
    SearchedItem(timetableItemWithFavorite = timeTableWithFavorite)
}

@Preview
@Composable
private fun SearchTextFieldPreview() {
    SearchTextField()
}