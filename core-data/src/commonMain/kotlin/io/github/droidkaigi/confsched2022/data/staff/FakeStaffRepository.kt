package io.github.droidkaigi.confsched2022.data.staff

import io.github.droidkaigi.confsched2022.model.Staff
import io.github.droidkaigi.confsched2022.model.StaffRepository
import io.github.droidkaigi.confsched2022.model.fakes
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeStaffRepository : StaffRepository {
    override fun staff(): Flow<PersistentList<Staff>> {
        return flowOf(Staff.fakes())
    }
}
