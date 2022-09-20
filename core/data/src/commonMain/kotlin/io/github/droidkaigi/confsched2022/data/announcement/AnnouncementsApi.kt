package io.github.droidkaigi.confsched2022.data.announcement

import io.github.droidkaigi.confsched2022.BuildKonfig
import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.announcement.response.AnnouncementsResponse
import io.github.droidkaigi.confsched2022.data.toInstantAsJST
import io.github.droidkaigi.confsched2022.model.Announcement
import io.github.droidkaigi.confsched2022.model.AnnouncementsByDate
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

public class AnnouncementsApi(
    private val networkService: NetworkService,
) {
    public suspend fun announcements(
        language: String
    ): PersistentList<AnnouncementsByDate> {
        return networkService.get<AnnouncementsResponse>(
            url = "${BuildKonfig.apiUrl}/events/droidkaigi2022/announcements/$language"
        )
            .toAnnouncementList()
            .toAnnouncementsByDate()
    }
}

private fun AnnouncementsResponse.toAnnouncementList(): PersistentList<Announcement> {
    val systemTZ = TimeZone.currentSystemDefault()

    return announcements.map { response ->
        Announcement(
            id = response.id,
            title = response.title,
            content = response.content,
            type = response.type.lowercase().replaceFirstChar { it.uppercase() },
            publishedAt = response.publishedAt.toInstantAsJST().toLocalDateTime(systemTZ).date,
            language = response.language,
        )
    }.toPersistentList()
}

private fun PersistentList<Announcement>.toAnnouncementsByDate():
    PersistentList<AnnouncementsByDate> = groupBy { it.publishedAt }.map {
    AnnouncementsByDate(
        publishedAt = it.key,
        announcements = it.value.toPersistentList()
    )
}.toPersistentList()
