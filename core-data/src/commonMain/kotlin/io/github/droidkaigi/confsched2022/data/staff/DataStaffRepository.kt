package io.github.droidkaigi.confsched2022.data.staff

import io.github.droidkaigi.confsched2022.model.Staff
import io.github.droidkaigi.confsched2022.model.StaffRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DataStaffRepository(
    private val staffApi: StaffApi
) : StaffRepository {
    override fun staff(): Flow<PersistentList<Staff>> {
        return callbackFlow {
            send(
                staffApi
                    .staff()
                    .filter { !it.username.lowercase().contains("bot") }
                    .toPersistentList()
            )
            awaitClose { }
        }
    }
}
