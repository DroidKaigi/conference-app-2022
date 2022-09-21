package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.model.DroidKaigi2022Day
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.Filters
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.fake
import io.github.droidkaigi.confsched2022.strings.Strings
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Error
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Loading
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Success
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun SearchRoot(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onBackIconClick: () -> Unit = {},
    onItemClick: (TimetableItemId) -> Unit,
) {
    val state: SearchUiModel by viewModel.uiModel
    SearchScreen(
        modifier = modifier,
        uiModel = state,
        onItemClick = onItemClick,
        onBookMarkClick = { sessionId, currentIsFavorite ->
            viewModel.onFavoriteToggle(sessionId, currentIsFavorite)
        },
        onBackIconClick = onBackIconClick
    )
}

@Composable
private fun SearchScreen(
    uiModel: SearchUiModel,
    onItemClick: (TimetableItemId) -> Unit,
    onBookMarkClick: (sessionId: TimetableItemId, currentIsFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onBackIconClick: () -> Unit = {},
) {
    val searchWord = rememberSaveable { mutableStateOf("") }
    KaigiScaffold(
        modifier = modifier,
        topBar = {},
        content = {
            Column(
                modifier = Modifier.windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical)
                )
            ) {
                when (uiModel.state) {
                    is Error -> TODO()
                    is Success -> {
                        SearchTextField(
                            searchWord = searchWord.value,
                            onSearchWordChange = { searchWord.value = it }
                        ) {
                            onBackIconClick()
                        }
                        SearchedItemListField(
                            schedule = uiModel.state.value,
                            searchWord = searchWord.value,
                            onItemClick = onItemClick,
                            onBookMarkClick = onBookMarkClick,
                        )
                    }
                    Loading -> {
                        FullScreenLoading()
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun SearchTextField(
    searchWord: String,
    onSearchWordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    TextField(
        value = searchWord,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .height(64.dp)
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .focusRequester(focusRequester),
        placeholder = { Text(stringResource(Strings.search_placeholder)) },
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                contentDescription = "arrow_back_icon",
                modifier = modifier
                    .clickable { onBackClick() }
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = "search_word_delete_icon",
                modifier = Modifier.clickable {
                    onSearchWordChange("")
                }
            )
        },
        onValueChange = {
            onSearchWordChange(it)
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SearchedItemListField(
    schedule: DroidKaigiSchedule,
    searchWord: String,
    onItemClick: (TimetableItemId) -> Unit,
    onBookMarkClick: (sessionId: TimetableItemId, currentIsFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        schedule.dayToTimetable.forEach { (dayToTimeTable, timeTable) ->
            val sessions =
                timeTable.filtered(Filters(filterSession = true, searchWord = searchWord)).contents
            if (sessions.isEmpty()) {
                return@forEach
            }
            stickyHeader {
                SearchedHeader(day = dayToTimeTable)
            }
            items(sessions) { timetableItemWithFavorite ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(timetableItemWithFavorite.timetableItem.id) }
                        .padding(16.dp)
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
                                val startLocalDateTime =
                                    timetableItemWithFavorite.timetableItem.startsAt
                                        .toLocalDateTime(TimeZone.of("UTC+9"))
                                val endLocalDateTime =
                                    timetableItemWithFavorite.timetableItem.endsAt
                                        .toLocalDateTime(TimeZone.of("UTC+9"))
                                val startTimeString = startLocalDateTime.time.toString()
                                val endTimeString = endLocalDateTime.time.toString()

                                Text(
                                    text = startTimeString,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Box(
                                    modifier = Modifier
                                        .size(1.dp, 2.dp)
                                        .background(MaterialTheme.colorScheme.onBackground)
                                ) { }
                                Text(
                                    text = endTimeString,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        SessionListItem(
                            timetableItem = timetableItemWithFavorite.timetableItem,
                            isFavorited = timetableItemWithFavorite.isFavorited,
                            onFavoriteClick = onBookMarkClick,
                            searchWord = searchWord,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchedHeader(day: DroidKaigi2022Day, modifier: Modifier = Modifier) {
    Text(
        text = day.name,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 16.dp),
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun FullScreenLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showSystemUi = false)
@Composable
fun SearchScreenPreview() {
    KaigiTheme {
        SearchScreen(
            uiModel = SearchUiModel(
                state = Success(DroidKaigiSchedule.fake())
            ),
            onItemClick = {},
            onBookMarkClick = { _, _ -> },
        )
    }
}
