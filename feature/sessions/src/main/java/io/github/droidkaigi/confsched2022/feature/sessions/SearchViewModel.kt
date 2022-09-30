package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock.ContextClock
import co.touchlab.kermit.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched2022.model.AppError
import io.github.droidkaigi.confsched2022.model.DroidKaigi2022Day
import io.github.droidkaigi.confsched2022.model.Filters
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.TimetableCategory
import io.github.droidkaigi.confsched2022.model.TimetableItem
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
    sessionsZipline: SessionsZipline,
    private val sessionAlarm: SessionAlarm,
) : ViewModel() {
    private val filters = mutableStateOf(Filters())

    private val filterSheetState = mutableStateOf<SearchFilterSheetState>(
        SearchFilterSheetState.Hide
    )
    private var appError by mutableStateOf<AppError?>(null)

    val uiModel: State<SearchUiModel> = run {
        val moleculeScope =
            CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

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
        moleculeScope.moleculeComposeState(clock = ContextClock) {
            val schedule by scheduleFlow.collectAsState(initial = UiLoadState.Loading)
            val filteredSchedule by remember(filters) {
                derivedStateOf {
                    schedule.mapSuccess { it.filtered(filters.value) }
                }
            }

            SearchUiModel(
                filter = SearchFilterUiModel(
                    selectedCategories = filters.value.categories,
                    selectedDays = filters.value.days,
                    isFavoritesOn = filters.value.filterFavorite
                ),
                filterSheetState = filterSheetState.value,
                state = filteredSchedule,
                appError = appError,
            )
        }
    }

    fun onFavoriteToggle(session: TimetableItem, currentIsFavorite: Boolean) {
        viewModelScope.launch {
            sessionsRepository.setFavorite(session.id, currentIsFavorite.not())
            sessionAlarm.toggleRegister(session, currentIsFavorite.not())
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

    fun onCategorySelected(categoryId: String) {
        viewModelScope.launch {
            val categories = sessionsRepository.getCategories()
            if (categories.isEmpty())
                return@launch

            filters.value = filters.value.copy(
                categories = listOf(
                    categories.first { it.id.toString() == categoryId }
                )
            )
        }
    }

    fun onDaySelected(day: DroidKaigi2022Day, isSelected: Boolean) {
        val selectedDays = filters.value.days.toMutableList()
        filters.value = filters.value.copy(
            days = selectedDays.apply {
                if (isSelected)
                    add(day)
                else
                    remove(day)
            }.sortedBy(DroidKaigi2022Day::start)
        )
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

    fun onSearchTextAreaClicked() {
        onFilterSheetDismissed()
    }

    init {
        refresh()
    }

    fun onRetryButtonClick() {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            try {
                sessionsRepository.refresh()
            } catch (e: AppError) {
                appError = e
            }
        }
    }

    fun onAppErrorNotified() {
        appError = null
    }
}
