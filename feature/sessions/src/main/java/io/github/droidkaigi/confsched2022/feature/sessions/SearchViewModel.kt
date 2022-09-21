package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import co.touchlab.kermit.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.model.DroidKaigi2022Day
import io.github.droidkaigi.confsched2022.model.Filters
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.TimetableCategory
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.ui.UiLoadState
import io.github.droidkaigi.confsched2022.ui.asLoadState
import io.github.droidkaigi.confsched2022.ui.moleculeComposeState
import io.github.droidkaigi.confsched2022.zipline.SessionsZipline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val sessionsRepository: SessionsRepository,
    sessionsZipline: SessionsZipline
) : ViewModel() {
    private val moleculeScope =
        CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    val uiModel: State<SearchUiModel>

    private val filters = mutableStateOf(Filters())

    private val filterSheetState = mutableStateOf<SearchFilterSheetState>(
        SearchFilterSheetState.Hide
    )

    init {
        val ziplineScheduleModifierFlow = sessionsZipline.timetableModifier()
        val sessionScheduleFlow = sessionsRepository.droidKaigiScheduleFlow()

        val scheduleFlow = combine(
            ziplineScheduleModifierFlow,
            sessionScheduleFlow,
            ::Pair
        ).map { (modifier, schedule) ->
            try {
                modifier(schedule)
            } catch (e: Exception) {
                Logger.d(throwable = e) { "Zipline modifier error" }
                schedule
            }
        }.asLoadState()

        uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
            val schedule by scheduleFlow.collectAsState(initial = UiLoadState.Loading)

            val filteredSchedule by remember(filters) {
                derivedStateOf {
                    schedule.mapSuccess { it.filtered(filters.value) }
                }
            }

            SearchUiModel(
                filter = SearchFilterUiModel(
                    selectedCategories = filters.value.categories,
                    selectedDay = filters.value.day,
                    isFavoritesOn = filters.value.filterFavorite
                ),
                filterSheetState = filterSheetState.value,
                state = filteredSchedule
            )
        }
    }

    fun onFavoriteToggle(sessionId: TimetableItemId, currentIsFavorite: Boolean) {
        viewModelScope.launch {
            sessionsRepository.setFavorite(sessionId, currentIsFavorite.not())
        }
    }

    fun onCategoriesSelected(category: TimetableCategory, isSelected: Boolean) {
        val selectedCategories = filters.value.categories.toMutableList()
        filters.value = filters.value.copy(
            categories = selectedCategories.apply {
                if (isSelected)
                    add(category)
                else
                    remove(category)
            }
        )
    }

    fun onDaySelected(day: DroidKaigi2022Day) {
        filters.value = filters.value.copy(day = day)
        filterSheetState.value = SearchFilterSheetState.Hide
    }

    fun onFilterFavoritesToggle() {
        filters.value = filters.value.copy(
            filterFavorite = !filters.value.filterFavorite
        )
    }

    fun onFilterDayClicked() {
        filterSheetState.value = SearchFilterSheetState.ShowDayFilter(
            days = DroidKaigi2022Day.values().toList()
        )
    }

    fun onFilterCategoriesClicked() {
        viewModelScope.launch {
            val categories = sessionsRepository.getCategories()
            if (categories.isEmpty())
                return@launch

            filterSheetState.value = SearchFilterSheetState.ShowCategoriesFilterSheet(
                categories = categories
            )
        }
    }

    fun onFilterSheetDismissed() {
        filterSheetState.value = SearchFilterSheetState.Hide
    }
}
