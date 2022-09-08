package io.github.droidkaigi.confsched2022.data.sessions

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.github.droidkaigi.confsched2022.data.Database
import io.github.droidkaigi.confsched2022.data.DatabaseService
import io.github.droidkaigi.confsched2022.data.TimetableItemSession
import io.github.droidkaigi.confsched2022.data.TimetableItemSpeaker
import io.github.droidkaigi.confsched2022.data.TimetableItemSpecial
import io.github.droidkaigi.confsched2022.model.MultiLangText
import io.github.droidkaigi.confsched2022.model.Timetable
import io.github.droidkaigi.confsched2022.model.TimetableAsset
import io.github.droidkaigi.confsched2022.model.TimetableCategory
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.TimetableItemList
import io.github.droidkaigi.confsched2022.model.TimetableRoom
import io.github.droidkaigi.confsched2022.model.TimetableSpeaker
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.Instant

class SessionsDao(
    databaseService: DatabaseService,
) {
    private val database: Database = databaseService.database
    private val timetableItemSpeakerQueries = database.timetableItemSpeakerQueries
    private val timetableItemSpecialQueries = database.timetableItemSpecialQueries
    private val timetableItemSessionQueries = database.timetableItemSessionQueries

    fun selectAll(): Flow<Timetable> {
        val timetableItemSpeakers = timetableItemSpeakerQueries.selectAll().asFlow().mapToList()
        val timetableItemSessions = timetableItemSessionQueries.selectAll().asFlow().mapToList()
        val timetableItemSpecials = timetableItemSpecialQueries.selectAll().asFlow().mapToList()
        return combine(
            timetableItemSpeakers,
            timetableItemSessions,
            timetableItemSpecials,
        ) { speakers, sessions, specials ->
            Timetable(
                timetableItems = TimetableItemList(
                    (sessions.mapToSessionList(speakers) + specials.mapToSpecialList(speakers))
                        .sortedWith(
                            compareBy<TimetableItem> { it.startsAt }
                                .thenBy { it.room.sort }
                        )
                        .toPersistentList()
                )
            )
        }
    }

    fun insert(timetable: Timetable) {
        timetable.timetableItems.timetableItems.forEach { timetableItem ->
            when (timetableItem) {
                is TimetableItem.Session -> {
                    timetableItemSessionQueries.insert(timetableItem.toModel())
                }
                is TimetableItem.Special -> {
                    timetableItemSpecialQueries.insert(timetableItem.toModel())
                    timetableItem.speakers.forEach { speaker ->
                        val timetableItemSpeaker = TimetableItemSpeaker(
                            timetableItemId = timetableItem.id.value,
                            name = speaker.name,
                            iconUrl = speaker.iconUrl,
                            bio = speaker.bio,
                            tagLine = speaker.tagLine,
                        )
                        timetableItemSpeakerQueries.insert(timetableItemSpeaker)
                    }
                }
            }
        }
    }

    fun deleteAll() {
        timetableItemSpeakerQueries.deleteAll()
        timetableItemSpecialQueries.deleteAll()
        timetableItemSessionQueries.deleteAll()
    }

    private fun TimetableItem.Session.toModel(): TimetableItemSession {
        return TimetableItemSession(
            id = id.value,
            titleJa = title.jaTitle,
            titleEn = title.enTitle,
            startsAt = startsAt.toEpochMilliseconds(),
            endsAt = endsAt.toEpochMilliseconds(),
            categoryId = category.id,
            categoryTitleJa = category.title.jaTitle,
            categoryTitleEn = category.title.enTitle,
            roomId = room.id,
            roomNameJa = room.name.jaTitle,
            roomNameEn = room.name.enTitle,
            roomSort = room.sort,
            targetAudience = targetAudience,
            language = language,
            assetVideoUrl = asset.videoUrl,
            assetSlideUrl = asset.slideUrl,
            levels = levelsAdapter.encode(levels),
            description = description,
            messageJa = message?.jaTitle,
            messageEn = message?.enTitle,
        )
    }

    private fun TimetableItem.Special.toModel(): TimetableItemSpecial {
        return TimetableItemSpecial(
            id = id.value,
            titleJa = title.jaTitle,
            titleEn = title.enTitle,
            startsAt = startsAt.toEpochMilliseconds(),
            endsAt = endsAt.toEpochMilliseconds(),
            categoryId = category.id,
            categoryTitleJa = category.title.jaTitle,
            categoryTitleEn = category.title.enTitle,
            roomId = room.id,
            roomNameJa = room.name.jaTitle,
            roomNameEn = room.name.enTitle,
            roomSort = room.sort,
            targetAudience = targetAudience,
            language = language,
            assetVideoUrl = asset.videoUrl,
            assetSlideUrl = asset.slideUrl,
            levels = levelsAdapter.encode(levels),
        )
    }

    private fun List<TimetableItemSession>.mapToSessionList(
        timetableItemSpeakers: List<TimetableItemSpeaker>,
    ): List<TimetableItem.Session> {
        return map { session ->
            val speakers = timetableItemSpeakers.filter { it.timetableItemId == session.id }
                .map { it.toSpeaker() }
                .toPersistentList()
            TimetableItem.Session(
                id = TimetableItemId(session.id),
                title = MultiLangText(
                    jaTitle = session.titleJa,
                    enTitle = session.titleEn,
                ),
                startsAt = Instant.fromEpochMilliseconds(session.startsAt),
                endsAt = Instant.fromEpochMilliseconds(session.endsAt),
                category = TimetableCategory(
                    id = session.categoryId,
                    title = MultiLangText(
                        jaTitle = session.categoryTitleJa,
                        enTitle = session.categoryTitleEn,
                    )
                ),
                room = TimetableRoom(
                    id = session.roomId,
                    name = MultiLangText(
                        jaTitle = session.roomNameJa,
                        enTitle = session.roomNameEn,
                    ),
                    sort = session.roomSort,
                ),
                targetAudience = session.targetAudience,
                language = session.language,
                asset = TimetableAsset(
                    videoUrl = session.assetVideoUrl,
                    slideUrl = session.assetSlideUrl,
                ),
                levels = levelsAdapter.decode(session.levels),
                description = session.description,
                speakers = speakers,
                message = if (session.messageJa != null && session.messageEn != null) {
                    MultiLangText(
                        jaTitle = session.messageJa,
                        enTitle = session.messageEn,
                    )
                } else {
                    null
                },
            )
        }
    }

    private fun List<TimetableItemSpecial>.mapToSpecialList(
        timetableItemSpeakers: List<TimetableItemSpeaker>
    ): List<TimetableItem.Special> {
        return map { special ->
            val speakers = timetableItemSpeakers.filter { it.timetableItemId == special.id }
                .map { it.toSpeaker() }
                .toPersistentList()
            TimetableItem.Special(
                id = TimetableItemId(special.id),
                title = MultiLangText(
                    jaTitle = special.titleJa,
                    enTitle = special.titleEn,
                ),
                startsAt = Instant.fromEpochMilliseconds(special.startsAt),
                endsAt = Instant.fromEpochMilliseconds(special.endsAt),
                category = TimetableCategory(
                    id = special.categoryId,
                    title = MultiLangText(
                        jaTitle = special.categoryTitleJa,
                        enTitle = special.categoryTitleEn,
                    )
                ),
                room = TimetableRoom(
                    id = special.roomId,
                    name = MultiLangText(
                        jaTitle = special.roomNameJa,
                        enTitle = special.roomNameEn,
                    ),
                    sort = special.roomSort,
                ),
                targetAudience = special.targetAudience,
                language = special.language,
                asset = TimetableAsset(
                    videoUrl = special.assetVideoUrl,
                    slideUrl = special.assetSlideUrl,
                ),
                levels = levelsAdapter.decode(special.levels),
                speakers = speakers,
            )
        }
    }

    private fun TimetableItemSpeaker.toSpeaker(): TimetableSpeaker {
        return TimetableSpeaker(
            name = name,
            iconUrl = iconUrl,
            bio = bio,
            tagLine = tagLine,
        )
    }

    companion object {
        private val levelsAdapter: ColumnAdapter<PersistentList<String>, String> =
            object : ColumnAdapter<PersistentList<String>, String> {

                private val LEVELS_DELIMITER = ","

                override fun decode(databaseValue: String): PersistentList<String> {
                    return if (databaseValue.isEmpty()) {
                        persistentListOf()
                    } else {
                        databaseValue.split(LEVELS_DELIMITER).toPersistentList()
                    }
                }

                override fun encode(value: PersistentList<String>): String {
                    return value.joinToString(separator = LEVELS_DELIMITER)
                }
            }
    }
}
