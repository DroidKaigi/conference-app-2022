package io.github.droidkaigi.confsched2022.data.announcement.response

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
public data class AnnouncementsResponse(
    val status: String,
    val announcements: List<AnnouncementResponse>,
)

@Serializable
public data class AnnouncementResponse(
    val id: String,
    val title: String,
    val content: String,
    val type: String,
    val publishedAt: Instant,
    val language: String,
)
