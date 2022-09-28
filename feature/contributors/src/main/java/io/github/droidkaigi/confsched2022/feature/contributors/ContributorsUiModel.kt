package io.github.droidkaigi.confsched2022.feature.contributors

import io.github.droidkaigi.confsched2022.model.AppError
import io.github.droidkaigi.confsched2022.model.Contributor
import io.github.droidkaigi.confsched2022.ui.UiLoadState
import kotlinx.collections.immutable.PersistentList

data class ContributorsUiModel(
    val state: UiLoadState<PersistentList<Contributor>>,
    val appError: AppError?
)
