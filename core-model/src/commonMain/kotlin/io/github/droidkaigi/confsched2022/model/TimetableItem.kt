package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
sealed class TimetableItem(
    open val id: TimetableItemId,
    open val title: MultiLangText,
    open val startsAt: Instant,
    open val endsAt: Instant,
    open val category: TimetableCategory,
    open val targetAudience: String,
    open val language: String,
    open val asset: TimetableAsset,
    open val levels: ImmutableList<String>,
) {
    data class Session(
        override val id: TimetableItemId,
        override val title: MultiLangText,
        override val startsAt: Instant,
        override val endsAt: Instant,
        override val category: TimetableCategory,
        override val targetAudience: String,
        override val language: String,
        override val asset: TimetableAsset,
        override val levels: ImmutableList<String>,
        val description: String,
        val speakers: ImmutableList<TimetableSpeaker>,
        val message: MultiLangText?,
    ) : TimetableItem(
        id = id,
        title = title,
        startsAt = startsAt,
        endsAt = endsAt,
        category = category,
        targetAudience = targetAudience,
        language = language,
        asset = asset,
        levels = levels,
    )

    data class Special(
        override val id: TimetableItemId,
        override val title: MultiLangText,
        override val startsAt: Instant,
        override val endsAt: Instant,
        override val category: TimetableCategory,
        override val targetAudience: String,
        override val language: String,
        override val asset: TimetableAsset,
        override val levels: ImmutableList<String>,
        val speakers: ImmutableList<TimetableSpeaker> = persistentListOf(),
    ) : TimetableItem(
        id = id,
        title = title,
        startsAt = startsAt,
        endsAt = endsAt,
        category = category,
        targetAudience = targetAudience,
        language = language,
        asset = asset,
        levels = levels,
    )

    val startsTimeString: String by lazy {
        val localDate = startsAt.toLocalDateTime(TimeZone.currentSystemDefault())
        "${localDate.hour}".padStart(2, '0') + ":" + "${localDate.minute}".padStart(2, '0')
    }
}
