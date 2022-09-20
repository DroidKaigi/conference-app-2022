package io.github.droidkaigi.confsched2022.data.announcement

import io.github.droidkaigi.confsched2022.model.AnnouncementsByDate
import io.github.droidkaigi.confsched2022.model.AnnouncementsRepository
import io.github.droidkaigi.confsched2022.model.fakes
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

public class FakeAnnouncementsRepository : AnnouncementsRepository {
    override fun announcements(): Flow<PersistentList<AnnouncementsByDate>> {
        return flowOf(AnnouncementsByDate.fakes())
    }
}
