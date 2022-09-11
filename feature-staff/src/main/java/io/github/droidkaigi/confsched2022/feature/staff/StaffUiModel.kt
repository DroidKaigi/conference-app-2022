package io.github.droidkaigi.confsched2022.feature.staff

import io.github.droidkaigi.confsched2022.model.Staff
import io.github.droidkaigi.confsched2022.ui.UiLoadState
import kotlinx.collections.immutable.PersistentList

data class StaffUiModel(val state: UiLoadState<PersistentList<Staff>>)
