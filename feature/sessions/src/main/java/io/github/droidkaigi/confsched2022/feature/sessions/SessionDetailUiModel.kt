package io.github.droidkaigi.confsched2022.feature.sessions

import io.github.droidkaigi.confsched2022.model.TimetableItemWithFavorite
import io.github.droidkaigi.confsched2022.ui.UiLoadState

data class SessionDetailUiModel(val state: UiLoadState<TimetableItemWithFavorite>)
