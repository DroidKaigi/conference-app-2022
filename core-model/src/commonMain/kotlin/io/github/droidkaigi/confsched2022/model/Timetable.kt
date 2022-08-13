package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable

@Serializable
data class Timetable(
    val timetableItems: TimetableItemList = TimetableItemList(),
    val favorites: ImmutableSet<TimetableItemId> = persistentSetOf(),
) {
    val contents by lazy {
        timetableItems.map {
            it to favorites.contains(it.id)
        }
    }

    fun filtered(filters: Filters): Timetable {
        var timetableItems = timetableItems.toList()
        if (filters.filterFavorite) {
            timetableItems = timetableItems.filter { timetableItem ->
                favorites.contains(timetableItem.id)
            }
        }
        return copy(timetableItems = TimetableItemList(timetableItems.toImmutableList()))
    }
    companion object
}

fun Timetable?.orEmptyContents(): Timetable = this ?: Timetable()

fun Timetable.Companion.fake(): Timetable = Timetable(
    timetableItems = TimetableItemList(
        persistentListOf(
            TimetableItem.Special(
                id = TimetableItemId("1"),
                title = MultiLangText("ウェルカムトーク", "Welcome Talk"),
                startsAt = LocalDateTime.parse("2021-10-20T10:00:00")
                    .toInstant(TimeZone.of("UTC+9")),
                endsAt = LocalDateTime.parse("2021-10-20T10:20:00")
                    .toInstant(TimeZone.of("UTC+9")),
                category = TimetableCategory(
                    id = 28657,
                    title = MultiLangText("その他", "Other"),
                ),
                targetAudience = "TBW",
                language = "TBD",
                asset = TimetableAsset(null, null),
                levels = persistentListOf(
                    "BEGINNER",
                    "INTERMEDIATE",
                    "ADVANCED",
                ),
            ),
            TimetableItem.Session(
                id = TimetableItemId("2"),
                title = MultiLangText("DroidKaigiのアプリのアーキテクチャ", "DroidKaigi App Architecture"),
                startsAt = LocalDateTime.parse("2021-10-20T10:30:00")
                    .toInstant(TimeZone.of("UTC+9")),
                endsAt = LocalDateTime.parse("2021-10-20T10:50:00")
                    .toInstant(TimeZone.of("UTC+9")),
                category = TimetableCategory(
                    id = 28654,
                    title = MultiLangText(
                        "Android FrameworkとJetpack",
                        "Android Framework and Jetpack",
                    ),
                ),
                targetAudience = "For App developer アプリ開発者向け",
                language = "JAPANESE",
                asset = TimetableAsset(
                    videoUrl = "https://www.youtube.com/watch?v=hFdKCyJ-Z9A",
                    slideUrl = "https://droidkaigi.jp/2021/",
                ),
                speakers = persistentListOf(
                    TimetableSpeaker(
                        name = "taka",
                        iconUrl = "https://github.com/takahirom.png",
                        bio = "Likes Android",
                        tagLine = "Android Engineer"
                    ),
                    TimetableSpeaker(
                        name = "ry",
                        iconUrl = "https://github.com/ry-itto.png",
                        bio = "Likes iOS",
                        tagLine = "iOS Engineer",
                    ),
                ),
                description = "これはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。",
                message = null,
                levels = persistentListOf(
                    "INTERMEDIATE",
                ),
            ),
            TimetableItem.Special(
                id = TimetableItemId("3"),
                title = MultiLangText("Closing", "Closing"),
                startsAt = LocalDateTime.parse("2021-10-21T18:00:00")
                    .toInstant(TimeZone.of("UTC+9")),
                endsAt = LocalDateTime.parse("2021-10-21T18:20:00")
                    .toInstant(TimeZone.of("UTC+9")),
                targetAudience = "TBW",
                category = TimetableCategory(
                    id = 28657,
                    title = MultiLangText("その他", "Other"),
                ),
                language = "TBD",
                asset = TimetableAsset(null, null),
                levels = persistentListOf(
                    "BEGINNER",
                    "INTERMEDIATE",
                    "ADVANCED",
                ),
            ),
        )
    ),
    favorites = persistentSetOf()
)
