package io.github.droidkaigi.confsched2022.data.announcement

import io.github.droidkaigi.confsched2022.BuildKonfig
import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.announcement.response.AnnouncementsResponse
import io.github.droidkaigi.confsched2022.data.toInstantAsJST
import io.github.droidkaigi.confsched2022.model.Announcement
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

public class AnnouncementsApi(
    private val networkService: NetworkService,
) {
    public suspend fun announcements(
        language: String
    ): PersistentList<Announcement> {
        return networkService.get<AnnouncementsResponse>(
            url = "${BuildKonfig.apiUrl}/events/droidkaigi2022/announcements/$language"
        ).toAnnouncementList()
    }
}

private fun AnnouncementsResponse.toAnnouncementList(): PersistentList<Announcement> {
    return announcements.map { response ->
        Announcement(
            id = response.id,
            title = response.title,
            content = response.content,
            type = response.type.lowercase().replaceFirstChar { it.uppercase() },
            publishedAt = response.publishedAt.toInstantAsJST(),
            language = response.language,
        )
    }.toPersistentList()
}
