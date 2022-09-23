package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import kotlinx.serialization.Serializable

@Serializable
public data class AnnouncementsByDate(
    val publishedAt: LocalDate,
    val announcements: PersistentList<Announcement>,
) {
    public companion object
}

public fun AnnouncementsByDate.Companion.fakes(): PersistentList<AnnouncementsByDate> {
    val systemTZ = TimeZone.currentSystemDefault()
    val today = Clock.System.todayIn(systemTZ)
    val yesterday = today.minus(1, DateTimeUnit.DAY)
    val beforeYesterday = today.minus(2, DateTimeUnit.DAY)

    return persistentListOf(
        AnnouncementsByDate(
            publishedAt = today,
            persistentListOf(
                Announcement(
                    id = "1",
                    title = "ALERTお知らせタイトル",
                    content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
                    type = "Alert",
                    language = "japanese"
                ),
                Announcement(
                    id = "2",
                    title = "FEEDBACKお知らせタイトル",
                    content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
                    type = "Feedback",
                    language = "japanese"
                ),
                Announcement(
                    id = "3",
                    title = "NOTIFICATIONお知らせタイトル",
                    content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
                    type = "Notification",
                    language = "japanese"
                ),
            )
        ),
        AnnouncementsByDate(
            publishedAt = yesterday,
            persistentListOf(
                Announcement(
                    id = "4",
                    title = "ALERTお知らせタイトル",
                    content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
                    type = "Alert",
                    language = "japanese"
                ),
                Announcement(
                    id = "5",
                    title = "FEEDBACKお知らせタイトル",
                    content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
                    type = "Feedback",
                    language = "japanese"
                ),
                Announcement(
                    id = "6",
                    title = "NOTIFICATIONお知らせタイトル",
                    content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
                    type = "Notification",
                    language = "japanese"
                ),
            )
        ),
        AnnouncementsByDate(
            publishedAt = beforeYesterday,
            persistentListOf(
                Announcement(
                    id = "7",
                    title = "ALERTお知らせタイトル",
                    content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
                    type = "Alert",
                    language = "japanese"
                ),
                Announcement(
                    id = "8",
                    title = "FEEDBACKお知らせタイトル",
                    content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
                    type = "Feedback",
                    language = "japanese"
                ),
                Announcement(
                    id = "9",
                    title = "NOTIFICATIONお知らせタイトル",
                    content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
                    type = "Notification",
                    language = "japanese"
                ),
            )
        ),
    )
}
