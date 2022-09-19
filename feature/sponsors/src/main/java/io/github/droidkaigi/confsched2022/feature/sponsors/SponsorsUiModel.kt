package io.github.droidkaigi.confsched2022.feature.sponsors

import io.github.droidkaigi.confsched2022.model.Sponsor
import io.github.droidkaigi.confsched2022.ui.UiLoadState
import kotlinx.collections.immutable.PersistentList

data class SponsorsUiModel(
    val state: UiLoadState<PersistentList<Sponsor>>
)
