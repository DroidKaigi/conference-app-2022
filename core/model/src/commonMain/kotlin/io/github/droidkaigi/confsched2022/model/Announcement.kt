package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.serialization.Serializable

@Serializable
public data class Announcement(
    val id: Int,
    val title: String,
    val content: String,
    val type: String,
    val publishedAt: Instant,
    val language: String,
) {
    public companion object
}

public fun Announcement.Companion.fakes(): PersistentList<Announcement> {
    val systemTZ = TimeZone.currentSystemDefault()
    val today = Clock.System.now()
    val yesterday = today.minus(2, DateTimeUnit.DAY, systemTZ)
    val beforeYesterday = today.minus(3, DateTimeUnit.DAY, systemTZ)

    return persistentListOf(
        Announcement(
            id = 1,
            title = "ALERTお知らせタイトル",
            content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
            type = "Alert",
            publishedAt = today,
            language = "japanese"
        ),
        Announcement(
            id = 2,
            title = "FEEDBACKお知らせタイトル",
            content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
            type = "Alert",
            publishedAt = today,
            language = "japanese"
        ),
        Announcement(
            id = 3,
            title = "NOTIFICATIONお知らせタイトル",
            content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
            type = "Alert",
            publishedAt = today,
            language = "japanese"
        ),
        Announcement(
            id = 4,
            title = "ALERTお知らせタイトル",
            content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
            type = "Alert",
            publishedAt = yesterday,
            language = "japanese"
        ),
        Announcement(
            id = 5,
            title = "FEEDBACKお知らせタイトル",
            content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
            type = "Alert",
            publishedAt = yesterday,
            language = "japanese"
        ),
        Announcement(
            id = 6,
            title = "NOTIFICATIONお知らせタイトル",
            content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
            type = "Alert",
            publishedAt = yesterday,
            language = "japanese"
        ),
        Announcement(
            id = 7,
            title = "ALERTお知らせタイトル",
            content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
            type = "Alert",
            publishedAt = beforeYesterday,
            language = "japanese"
        ),
        Announcement(
            id = 8,
            title = "FEEDBACKお知らせタイトル",
            content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
            type = "Alert",
            publishedAt = beforeYesterday,
            language = "japanese"
        ),
        Announcement(
            id = 9,
            title = "NOTIFICATIONお知らせタイトル",
            content = "お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文お知らせ本文",
            type = "Alert",
            publishedAt = beforeYesterday,
            language = "japanese"
        ),
    )
}
