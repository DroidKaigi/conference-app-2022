package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.designsystem.theme.Typography
import io.github.droidkaigi.confsched2022.feature.common.AppErrorSnackbarEffect
import io.github.droidkaigi.confsched2022.model.DroidKaigi2022Day
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.Filters
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.empty
import io.github.droidkaigi.confsched2022.model.fake
import io.github.droidkaigi.confsched2022.strings.Strings
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Error
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Loading
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Success
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchRoot(
    modifier: Modifier = Modifier,
    initialCategoryId: String? = null,
    viewModel: SearchViewModel = hiltViewModel(),
    onBackIconClick: () -> Unit = {},
    onItemClick: (TimetableItemId) -> Unit,
) {
    val state: SearchUiModel by viewModel.uiModel

    val scaffoldState = rememberBottomSheetScaffoldState()

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(initialCategoryId) {
        if (initialCategoryId != null) {
            viewModel.onCategorySelected(initialCategoryId)
        }
    }

    LaunchedEffect(state.filterSheetState) {
        when (state.filterSheetState) {
            is SearchFilterSheetState.ShowDayFilter,
            is SearchFilterSheetState.ShowCategoriesFilterSheet -> {
                scaffoldState.bottomSheetState.expand()
            }

            else -> scaffoldState.bottomSheetState.collapse()
        }
    }

    LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
        if (scaffoldState.bottomSheetState.currentValue == BottomSheetValue.Collapsed)
            viewModel.onFilterSheetDismissed()
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            when (val sheetState = state.filterSheetState) {
                is SearchFilterSheetState.ShowDayFilter -> {
                    FilterDaySheet(
                        selectedDays = state.filter.selectedDays,
                        kaigiDays = sheetState.days,
                        onDaySelected = viewModel::onDaySelected,
                        onDismiss = viewModel::onFilterSheetDismissed
                    )
                }

                is SearchFilterSheetState.ShowCategoriesFilterSheet -> {
                    FilterCategoriesSheet(
                        selectedCategories = state.filter.selectedCategories,
                        categories = sheetState.categories,
                        onCategoriesSelected = viewModel::onCategoriesSelected,
                        onDismiss = viewModel::onFilterSheetDismissed
                    )
                }

                else -> {}
            }
        },
        sheetShape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        sheetPeekHeight = 0.dp,
        sheetBackgroundColor = MaterialTheme
            .colorScheme.surfaceColorAtElevation(2.dp),
        sheetElevation = 4.dp
    ) { padding ->
        SearchScreen(
            modifier = modifier.padding(paddingValues = padding),
            uiModel = state,
            onItemClick = onItemClick,
            onBookMarkClick = { sessionId, currentIsFavorite ->
                viewModel.onFavoriteToggle(sessionId, currentIsFavorite)
            },
            onDayFilterClicked = {
                keyboardController?.hide()
                viewModel.onFilterDayClicked()
            },
            onCategoriesFilteredClicked = {
                keyboardController?.hide()
                viewModel.onFilterCategoriesClicked()
            },
            onFavoritesToggleClicked = {
                keyboardController?.hide()
                viewModel.onFilterFavoritesToggle()
            },
            onBackIconClick = onBackIconClick,
            onRetryButtonClick = { viewModel.onRetryButtonClick() },
            onAppErrorNotified = { viewModel.onAppErrorNotified() },
            onSearchTextAreaClicked = {
                viewModel.onSearchTextAreaClicked()
            }
        )
    }
}

@Composable
private fun SearchScreen(
    uiModel: SearchUiModel,
    onItemClick: (TimetableItemId) -> Unit,
    onBookMarkClick: (sessionId: TimetableItemId, currentIsFavorite: Boolean) -> Unit,
    onDayFilterClicked: () -> Unit,
    onCategoriesFilteredClicked: () -> Unit,
    onFavoritesToggleClicked: () -> Unit,
    onSearchTextAreaClicked: () -> Unit,
    onBackIconClick: () -> Unit,
    onRetryButtonClick: () -> Unit,
    onAppErrorNotified: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val searchWord = rememberSaveable { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    KaigiScaffold(
        snackbarHostState = snackbarHostState,
        modifier = modifier,
        topBar = {
            if (uiModel.state is Success) {
                SearchTextFieldTopAppBar(
                    searchWord = searchWord.value,
                    onSearchWordChange = { searchWord.value = it },
                    onSearchTextAreaClicked = onSearchTextAreaClicked,
                    onBackClick = onBackIconClick
                )
            }
        },
        content = {
            AppErrorSnackbarEffect(
                appError = uiModel.appError,
                snackBarHostState = snackbarHostState,
                onAppErrorNotified = onAppErrorNotified,
                onRetryButtonClick = onRetryButtonClick
            )
            Column(
                modifier = Modifier.padding(paddingValues = it)
            ) {
                when (uiModel.state) {
                    is Error -> {
                        // Do nothing
                    }
                    is Success -> {
                        SearchFilter(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(state = rememberScrollState())
                                .padding(vertical = 16.dp),
                            model = uiModel.filter,
                            onDayClicked = onDayFilterClicked,
                            onCategoryClicked = onCategoriesFilteredClicked,
                            onFavoritesClicked = onFavoritesToggleClicked
                        )
                        val foundSomeResults =
                            uiModel.state.value.dayToTimetable.any { (_, timeTable) ->
                                timeTable
                                    .filtered(
                                        Filters(filterSession = true, searchWord = searchWord.value)
                                    )
                                    .isEmpty()
                                    .not()
                            }
                        if (foundSomeResults) {
                            SearchedItemListField(
                                schedule = uiModel.state.value,
                                searchWord = searchWord.value,
                                onItemClick = onItemClick,
                                onBookMarkClick = onBookMarkClick,
                            )
                        } else {
                            SearchEmptyScreen()
                        }
                    }
                    Loading -> {
                        FullScreenLoading()
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextFieldTopAppBar(
    searchWord: String,
    onSearchWordChange: (String) -> Unit,
    onSearchTextAreaClicked: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    Column {
        TopAppBar(
            modifier = modifier,
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                        contentDescription = "back"
                    )
                }
            },
            title = {
                SearchTextField(
                    modifier = Modifier
                        .background(color = backgroundColor),
                    searchWord = searchWord,
                    onSearchWordChange = onSearchWordChange,
                    onSearchTextAreaClicked = onSearchTextAreaClicked
                )
            },
            colors = TopAppBarDefaults
                .smallTopAppBarColors(containerColor = backgroundColor)
        )
        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun SearchTextField(
    searchWord: String,
    onSearchWordChange: (String) -> Unit,
    onSearchTextAreaClicked: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = Typography.titleMedium,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect {
            if (it is PressInteraction.Release) {
                onSearchTextAreaClicked()
            }
        }
    }

    BasicTextField(
        value = searchWord,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .height(64.dp)
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .focusRequester(focusRequester),
        onValueChange = onSearchWordChange,
        textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        interactionSource = interactionSource,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = searchWord,
                visualTransformation = VisualTransformation.None,
                innerTextField = innerTextField,
                placeholder = { Text(stringResource(Strings.search_placeholder)) },
                trailingIcon = {
                    IconButton(
                        onClick = { onSearchWordChange("") }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = "search_word_delete_icon",
                        )
                    }
                },
                singleLine = true,
                enabled = true,
                colors = TextFieldDefaults.textFieldColors(),
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = 16.dp,
                    start = 0.dp,
                    end = 16.dp
                )
            )
        }
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
                val actionLabel = stringResource(
                    if (timetableItemWithFavorite.isFavorited) {
                        Strings.unregister_favorite_action_label
                    } else {
                        Strings.register_favorite_action_label
                    }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(timetableItemWithFavorite.timetableItem.id) }
                        .padding(16.dp)
                        .semantics(mergeDescendants = true) {
                            customActions = listOf(
                                CustomAccessibilityAction(
                                    label = actionLabel,
                                    action = {
                                        onBookMarkClick(
                                            timetableItemWithFavorite.timetableItem.id,
                                            timetableItemWithFavorite.isFavorited
                                        )
                                        true
                                    }
                                )
                            )
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .width(85.dp)
                            // Remove time semantics so description is set in SessionListItem
                            .clearAndSetSemantics { },
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

@Composable
fun SearchFilter(
    model: SearchFilterUiModel,
    onDayClicked: () -> Unit,
    onCategoryClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        FilterButton(
            isSelected = model.isDaySelected,
            isDropDown = true,
            text = model.selectedDaysValues.ifEmpty {
                stringResource(id = Strings.search_filter_select_day.resourceId)
            },
            onClicked = onDayClicked
        )

        FilterButton(
            isSelected = model.isCategoriesSelected,
            text = model.selectedCategoriesValue.ifEmpty {
                stringResource(id = Strings.search_filter_select_category.resourceId)
            },
            isDropDown = true,
            onClicked = onCategoryClicked
        )

        FilterButton(
            isSelected = model.isFavoritesOn,
            isDropDown = false,
            text = stringResource(id = Strings.search_filter_favorites.resourceId),
            onClicked = onFavoritesClicked
        )

        Spacer(modifier = Modifier.width(8.dp))
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

@Composable
private fun SearchEmptyScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(58.dp),
                tint = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = stringResource(Strings.search_empty_result.resourceId),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Preview(showSystemUi = false)
@Composable
fun SearchScreenPreview() {
    KaigiTheme {
        SearchScreen(
            uiModel = SearchUiModel(
                filter = SearchFilterUiModel(),
                filterSheetState = SearchFilterSheetState.Hide,
                state = Success(DroidKaigiSchedule.fake()),
                appError = null,
            ),
            onItemClick = {},
            onBookMarkClick = { _, _ -> },
            onBackIconClick = {},
            onFavoritesToggleClicked = {},
            onDayFilterClicked = {},
            onCategoriesFilteredClicked = {},
            onRetryButtonClick = {},
            onAppErrorNotified = {},
            onSearchTextAreaClicked = {},
        )
    }
}

@Preview(showSystemUi = false)
@Composable
fun SearchEmptyScreenPreview() {
    KaigiTheme {
        SearchScreen(
            uiModel = SearchUiModel(
                filter = SearchFilterUiModel(),
                filterSheetState = SearchFilterSheetState.Hide,
                state = Success(DroidKaigiSchedule.empty()),
                appError = null,
            ),
            onItemClick = {},
            onBookMarkClick = { _, _ -> },
            onBackIconClick = {},
            onFavoritesToggleClicked = {},
            onDayFilterClicked = {},
            onCategoriesFilteredClicked = {},
            onSearchTextAreaClicked = {},
            onAppErrorNotified = {},
            onRetryButtonClick = {}
        )
    }
}
