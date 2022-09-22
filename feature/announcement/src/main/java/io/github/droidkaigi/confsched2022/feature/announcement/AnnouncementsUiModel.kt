package io.github.droidkaigi.confsched2022.feature.announcement

import io.github.droidkaigi.confsched2022.model.AnnouncementsByDate
import io.github.droidkaigi.confsched2022.ui.UiLoadState
import kotlinx.collections.immutable.PersistentList

data class AnnouncementsUiModel(val state: UiLoadState<PersistentList<AnnouncementsByDate>>)
