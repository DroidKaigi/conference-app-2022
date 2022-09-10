package io.github.droidkaigi.confsched2022.feature.staff

import io.github.droidkaigi.confsched2022.model.Staff
import io.github.droidkaigi.confsched2022.ui.Result
import kotlinx.collections.immutable.PersistentList

data class StaffUiModel(
    val staffState: StaffState,
) {
    sealed interface StaffState {

        data class Loaded(val staff: PersistentList<Staff>) : StaffState

        object Loading : StaffState

        companion object {
            fun of(staffResult: Result<PersistentList<Staff>>): StaffState {
                return when (staffResult) {
                    is Result.Loading -> {
                        Loading
                    }
                    is Result.Success -> {
                        Loaded(staffResult.data)
                    }
                    is Result.Error -> {
                        staffResult.exception?.printStackTrace()
                        Loading
                    }
                }
            }
        }
    }
}
