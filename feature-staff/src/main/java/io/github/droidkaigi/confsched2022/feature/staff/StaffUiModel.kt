package io.github.droidkaigi.confsched2022.feature.staff

import android.util.Log
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
                        Log.d("nono", "loading")
                        Loading
                    }
                    is Result.Success -> {
                        Log.d("nono", "success : ${staffResult.data}")
                        Loaded(staffResult.data)
                    }
                    is Result.Error -> {
                        Log.d("nono", "error:${staffResult.exception}")
                        staffResult.exception?.printStackTrace()
                        Loading
                    }
                }
            }
        }
    }
}
