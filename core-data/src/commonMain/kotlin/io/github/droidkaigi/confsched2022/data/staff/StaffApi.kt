package io.github.droidkaigi.confsched2022.data.staff

import io.github.droidkaigi.confsched2022.BuildKonfig
import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.staff.response.StaffResponse
import io.github.droidkaigi.confsched2022.model.Staff
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

public class StaffApi(
    private val networkService: NetworkService
) {
    public suspend fun staff(): PersistentList<Staff> {
        return networkService.get<StaffResponse>(
            url = "${BuildKonfig.apiUrl}/events/droidkaigi2022/staff"
        ).toStaffList()
    }
}

private fun StaffResponse.toStaffList(): PersistentList<Staff> {
    return staff.map {
        Staff(
            id = it.id,
            username = it.username,
            profileUrl = it.profileUrl,
            iconUrl = it.iconUrl
        )
    }.toPersistentList()
}
