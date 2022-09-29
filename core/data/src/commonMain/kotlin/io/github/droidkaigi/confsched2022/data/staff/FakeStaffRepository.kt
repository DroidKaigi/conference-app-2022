package io.github.droidkaigi.confsched2022.data.staff

import io.github.droidkaigi.confsched2022.model.Staff
import io.github.droidkaigi.confsched2022.model.StaffRepository
import io.github.droidkaigi.confsched2022.model.fakes
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

public class FakeStaffRepository : StaffRepository {
    public val staff: PersistentList<Staff> = Staff.fakes()
    override fun staff(): Flow<PersistentList<Staff>> {
        return flowOf(staff)
    }

    override suspend fun refresh() {
    }
}
