@file:UseSerializers(
    PersistentSetSerializer::class
)

package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class Timetable(
    val timetableItems: TimetableItemList = TimetableItemList(),
    val favorites: ImmutableSet<TimetableItemId> = persistentSetOf(),
) {
    val contents by lazy {
        timetableItems.map {
            TimetableItemWithFavorite(it, favorites.contains(it.id))
        }
    }

    fun dayTimetable(droidKaigi2022Day: DroidKaigi2022Day): Timetable {
        var timetableItems = timetableItems.toList()
        timetableItems = timetableItems.filter { timetableItem ->
            timetableItem.day == droidKaigi2022Day
        }
        return copy(timetableItems = TimetableItemList(timetableItems.toPersistentList()))
    }

    fun filtered(filters: Filters): Timetable {
        var timetableItems = timetableItems.toList()
        if (filters.filterFavorite) {
            timetableItems = timetableItems.filter { timetableItem ->
                favorites.contains(timetableItem.id)
            }
        }
        return copy(timetableItems = TimetableItemList(timetableItems.toPersistentList()))
    }

    companion object
}

fun Timetable?.orEmptyContents(): Timetable = this ?: Timetable()

fun Timetable.Companion.fake(): Timetable {
    val timetableItems = buildList {
        add(
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
                room = TimetableRoom(
                    1000,
                    MultiLangText("AAAAA JA", "AAAAA EN"),
                    0
                ),
                targetAudience = "TBW",
                language = "TBD",
                asset = TimetableAsset(null, null),
                levels = persistentListOf(
                    "BEGINNER",
                    "INTERMEDIATE",
                    "ADVANCED",
                ),
            )
        )
        (-1..1).forEach { day ->
            (0..20).forEach { index ->
                val dayOffset = day * 24 * 60 * 60
                val start = Instant.fromEpochSeconds(
                    LocalDateTime.parse("2021-10-20T10:10:00")
                        .toInstant(TimeZone.of("UTC+9")).epochSeconds + index * 25 * 60 + dayOffset
                ).toLocalDateTime(
                    TimeZone.of("UTC+9")
                )
                val end = Instant.fromEpochSeconds(
                    LocalDateTime.parse("2021-10-20T10:50:00")
                        .toInstant(TimeZone.of("UTC+9")).epochSeconds + index * 25 * 60 + dayOffset
                ).toLocalDateTime(
                    TimeZone.of("UTC+9")
                )

                add(
                    TimetableItem.Session(
                        id = TimetableItemId("2$index"),
                        title = MultiLangText(
                            "DroidKaigiのアプリのアーキテクチャ$index",
                            "DroidKaigi App Architecture$index"
                        ),
                        startsAt = start
                            .toInstant(TimeZone.of("UTC+9")),
                        endsAt = end
                            .toInstant(TimeZone.of("UTC+9")),
                        category = TimetableCategory(
                            id = 28654,
                            title = MultiLangText(
                                "Android FrameworkとJetpack",
                                "Android Framework and Jetpack",
                            ),
                        ),
                        room = TimetableRoom(
                            1000 + index % 3,
                            MultiLangText("${index % 3} JA", "${index % 3} EN"),
                            0 + index % 3
                        ),
                        targetAudience = "For App developer アプリ開発者向け",
                        language = "JAPANESE",
                        asset = TimetableAsset(
                            videoUrl = "https://www.youtube.com/watch?v=hFdKCyJ-Z9A",
                            slideUrl = "https://droidkaigi.jp/2021/",
                        ),
                        levels = persistentListOf(
                            "INTERMEDIATE",
                        ),
                        description = "これはディスクリプションです。\n" +
                            "これはディスクリプションです。\n" +
                            "これはディスクリプションです。\n" +
                            "これはディスクリプションです。",
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
                        message = null,
                    )
                )
            }
        }
        add(
            TimetableItem.Special(
                id = TimetableItemId("3"),
                title = MultiLangText("Closing", "Closing"),
                startsAt = LocalDateTime.parse("2021-10-20T10:40:00")
                    .toInstant(TimeZone.of("UTC+9")),
                endsAt = LocalDateTime.parse("2021-10-20T11:00:00")
                    .toInstant(TimeZone.of("UTC+9")),
                category = TimetableCategory(
                    id = 28657,
                    title = MultiLangText("その他", "Other"),
                ),
                room = TimetableRoom(
                    2000,
                    MultiLangText("BBBB JA", "BBBB EN"),
                    0
                ),
                targetAudience = "TBW",
                language = "TBD",
                asset = TimetableAsset(null, null),
                levels = persistentListOf(
                    "BEGINNER",
                    "INTERMEDIATE",
                    "ADVANCED",
                ),
            )
        )
    }
    return Timetable(
        timetableItems = TimetableItemList(
            timetableItems.toPersistentList()
        ),
        favorites = persistentSetOf()
    )
}
