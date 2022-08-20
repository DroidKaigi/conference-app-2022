@file:UseSerializers(
    PersistentListSerializer::class,
    ImmutableListSerializer::class,
    ImmutableSetSerializer::class
)
package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
sealed class TimetableItem {
    abstract val id: TimetableItemId
    abstract val title: MultiLangText
    abstract val startsAt: Instant
    abstract val endsAt: Instant
    abstract val category: TimetableCategory
    abstract val room: TimetableRoom
    abstract val targetAudience: String
    abstract val language: String
    abstract val asset: TimetableAsset
    abstract val levels: ImmutableList<String>

    @Serializable
    data class Session(
        override val id: TimetableItemId,
        override val title: MultiLangText,
        override val startsAt: Instant,
        override val endsAt: Instant,
        override val category: TimetableCategory,
        override val room: TimetableRoom,
        override val targetAudience: String,
        override val language: String,
        override val asset: TimetableAsset,
        override val levels: ImmutableList<String>,
        val description: String,
        val speakers: ImmutableList<TimetableSpeaker>,
        val message: MultiLangText?,
    ) : TimetableItem()

    @Serializable
    data class Special(
        override val id: TimetableItemId,
        override val title: MultiLangText,
        override val startsAt: Instant,
        override val endsAt: Instant,
        override val category: TimetableCategory,
        override val room: TimetableRoom,
        override val targetAudience: String,
        override val language: String,
        override val asset: TimetableAsset,
        override val levels: ImmutableList<String>,
        val speakers: ImmutableList<TimetableSpeaker> = persistentListOf(),
    ) : TimetableItem()

    val startsTimeString: String by lazy {
        val localDate = startsAt.toLocalDateTime(TimeZone.currentSystemDefault())
        "${localDate.hour}".padStart(2, '0') + ":" + "${localDate.minute}".padStart(2, '0')
    }
}
