package io.github.droidkaigi.confsched2022.data.announcement

import io.github.droidkaigi.confsched2022.model.AnnouncementsByDate
import io.github.droidkaigi.confsched2022.model.AnnouncementsRepository
import io.github.droidkaigi.confsched2022.model.Lang.JAPANESE
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull

public class DataAnnouncementsRepository(
    private val announcementsApi: AnnouncementsApi
) : AnnouncementsRepository {
    private val announcementStateFlow =
        MutableStateFlow<PersistentList<AnnouncementsByDate>?>(null)

    override fun announcements(): Flow<PersistentList<AnnouncementsByDate>> {
        return callbackFlow {
            announcementStateFlow.filterNotNull().collect {
                send(it)
            }
        }
    }

    override suspend fun refresh() {
        announcementStateFlow.value = announcementsApi
            .announcements(JAPANESE.name.lowercase())
    }
}
