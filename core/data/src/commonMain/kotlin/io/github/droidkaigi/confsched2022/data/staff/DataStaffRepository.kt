package io.github.droidkaigi.confsched2022.data.staff

import io.github.droidkaigi.confsched2022.model.Staff
import io.github.droidkaigi.confsched2022.model.StaffRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow

public class DataStaffRepository(
    private val staffApi: StaffApi
) : StaffRepository {
    private val staffStateFlow =
        MutableStateFlow<PersistentList<Staff>>(persistentListOf())

    override fun staff(): Flow<PersistentList<Staff>> {
        return callbackFlow {
            staffStateFlow.collect {
                send(it)
            }
        }
    }

    override suspend fun refresh() {
        staffStateFlow.value = staffApi
            .staff()
            .toPersistentList()
    }
}
