package io.github.droidkaigi.confsched2022.data.announcement

import io.github.droidkaigi.confsched2022.model.AnnouncementsByDate
import io.github.droidkaigi.confsched2022.model.AnnouncementsRepository
import io.github.droidkaigi.confsched2022.model.Lang.JAPANESE
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow

public class DataAnnouncementsRepository(
    private val announcementsApi: AnnouncementsApi
) : AnnouncementsRepository {
    private val announcementStateFlow =
        MutableStateFlow<PersistentList<AnnouncementsByDate>>(persistentListOf())

    override fun announcements(): Flow<PersistentList<AnnouncementsByDate>> {
        return callbackFlow {
            announcementStateFlow.collect {
                send(it)
            }
        }
    }

    override suspend fun refresh() {
        announcementStateFlow.value = announcementsApi
            .announcements(JAPANESE.name.lowercase())
    }
}
