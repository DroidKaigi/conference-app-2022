package io.github.droidkaigi.confsched2022.data.announcement

import io.github.droidkaigi.confsched2022.BuildKonfig
import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.announcement.response.AnnouncementsResponse
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
            url = "${BuildKonfig.apiUrl}/announcements/$language"
        ).toAnnouncementsByDate()
    }
}

private fun AnnouncementsResponse.toAnnouncementsByDate(): PersistentList<AnnouncementsByDate> {
    val systemTZ = TimeZone.currentSystemDefault()

    return announcements
        .groupBy { response ->
            response.publishedAt.toLocalDateTime(systemTZ).date
        }
        .map {
            AnnouncementsByDate(
                publishedAt = it.key,
                announcements = it.value.map { response ->
                    Announcement(
                        id = response.id,
                        title = response.title,
                        content = response.content,
                        type = response.type.lowercase().replaceFirstChar(Char::uppercase),
                        language = response.language,
                    )
                }.toPersistentList()
            )
        }.toPersistentList()
}
