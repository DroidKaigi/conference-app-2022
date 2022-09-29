package io.github.droidkaigi.confsched2022.data.announcement

import io.github.droidkaigi.confsched2022.model.AnnouncementsByDate
import io.github.droidkaigi.confsched2022.model.AnnouncementsRepository
import io.github.droidkaigi.confsched2022.model.Lang.ENGLISH
import io.github.droidkaigi.confsched2022.model.Lang.JAPANESE
import io.github.droidkaigi.confsched2022.model.Lang.MIXED
import io.github.droidkaigi.confsched2022.model.defaultLang
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
            .announcements(
                when (defaultLang()) {
                    MIXED -> "english"
                    JAPANESE -> "japanese"
                    ENGLISH -> "english"
                }
            )
    }
}
