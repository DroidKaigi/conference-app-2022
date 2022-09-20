package io.github.droidkaigi.confsched2022.data.announcement

import io.github.droidkaigi.confsched2022.model.Announcement
import io.github.droidkaigi.confsched2022.model.AnnouncementRepository
import io.github.droidkaigi.confsched2022.model.fakes
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

public class FakeAnnouncementRepository : AnnouncementRepository {
    override fun announcements(): Flow<PersistentList<Announcement>> {
        return flowOf(Announcement.fakes())
    }
}
