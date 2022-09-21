package io.github.droidkaigi.confsched2022.data.announcement

import io.github.droidkaigi.confsched2022.model.AnnouncementsByDate
import io.github.droidkaigi.confsched2022.model.AnnouncementsRepository
import io.github.droidkaigi.confsched2022.model.defaultLang
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

public class DataAnnouncementsRepository(
    private val announcementsApi: AnnouncementsApi
) : AnnouncementsRepository {
    override fun announcements(): Flow<PersistentList<AnnouncementsByDate>> {
        return callbackFlow {
            send(
                announcementsApi
                    .announcements(defaultLang().name.lowercase())
            )
        }
    }
}
