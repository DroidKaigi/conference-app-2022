package io.github.droidkaigi.confsched2022.data.sessions

import io.github.droidkaigi.confsched2022.BuildKonfig
import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.sessions.response.LocaledResponse
import io.github.droidkaigi.confsched2022.data.sessions.response.SessionAllResponse
import io.github.droidkaigi.confsched2022.data.sessions.response.SessionAssetResponse
import io.github.droidkaigi.confsched2022.data.sessions.response.SessionMessageResponse
import io.github.droidkaigi.confsched2022.data.toInstantAsJST
import io.github.droidkaigi.confsched2022.model.MultiLangText
import io.github.droidkaigi.confsched2022.model.Timetable
import io.github.droidkaigi.confsched2022.model.TimetableAsset
import io.github.droidkaigi.confsched2022.model.TimetableCategory
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.TimetableItemList
import io.github.droidkaigi.confsched2022.model.TimetableLanguage
import io.github.droidkaigi.confsched2022.model.TimetableRoom
import io.github.droidkaigi.confsched2022.model.TimetableSpeaker
import kotlinx.collections.immutable.toPersistentList

public class SessionsApi(
    private val networkService: NetworkService,
) {
    public suspend fun timetable(): Timetable {
        return networkService
            .get<SessionAllResponse>(
                url = "${BuildKonfig.apiUrl}/events/droidkaigi2022/timetable"
            )
            .toTimetable()
    }
}

internal fun SessionAllResponse.toTimetable(): Timetable {
    val timetableContents = this
    val speakerIdToSpeaker: Map<String, TimetableSpeaker> = timetableContents.speakers
        .groupBy { it.id }
        .mapValues { (_, apiSpeakers) ->
            apiSpeakers.map { apiSpeaker ->
                TimetableSpeaker(
                    id = apiSpeaker.id,
                    name = apiSpeaker.fullName,
                    bio = apiSpeaker.bio,
                    iconUrl = apiSpeaker.profilePicture.orEmpty(),
                    tagLine = apiSpeaker.tagLine,
                )
            }.first()
        }
    val categoryIdToCategory: Map<Int, TimetableCategory> = timetableContents.categories
        .flatMap { it.items }
        .groupBy { it.id }
        .mapValues { (_, apiCategories) ->
            apiCategories.map { apiCategory ->
                TimetableCategory(
                    id = apiCategory.id,
                    title = apiCategory.name.toMultiLangText()
                )
            }.first()
        }
    val roomIdToRoom = timetableContents.rooms
        .groupBy { it.id }
        .mapValues { (_, apiRooms) ->
            apiRooms.map { apiRoom ->
                TimetableRoom(
                    id = apiRoom.id,
                    name = apiRoom.name.toMultiLangText(),
                    sort = apiRoom.sort,
                )
            }.first()
        }

    return Timetable(
        TimetableItemList(
            timetableContents.sessions.map { apiSession ->
                if (!apiSession.isServiceSession) {
                    TimetableItem.Session(
                        id = TimetableItemId(apiSession.id),
                        title = apiSession.title.toMultiLangText(),
                        startsAt = apiSession.startsAt.toInstantAsJST(),
                        endsAt = apiSession.endsAt.toInstantAsJST(),
                        category = categoryIdToCategory[apiSession.sessionCategoryItemId]!!,
                        room = roomIdToRoom[apiSession.roomId]!!,
                        targetAudience = apiSession.targetAudience,
                        language = TimetableLanguage(
                            langOfSpeaker = apiSession.language,
                            isInterpretationTarget = apiSession.interpretationTarget,
                        ),
                        asset = apiSession.asset.toTimetableAsset(),
                        description = apiSession.description ?: "",
                        speakers = apiSession.speakers
                            .map { speakerIdToSpeaker[it]!! }
                            .toPersistentList(),
                        message = apiSession.message?.toMultiLangText(),
                        levels = apiSession.levels.toPersistentList(),
                    )
                } else {
                    TimetableItem.Special(
                        id = TimetableItemId(apiSession.id),
                        title = apiSession.title.toMultiLangText(),
                        startsAt = apiSession.startsAt.toInstantAsJST(),
                        endsAt = apiSession.endsAt.toInstantAsJST(),
                        category = categoryIdToCategory[apiSession.sessionCategoryItemId]!!,
                        room = roomIdToRoom[apiSession.roomId]!!,
                        targetAudience = apiSession.targetAudience,
                        language = TimetableLanguage(
                            langOfSpeaker = apiSession.language,
                            isInterpretationTarget = apiSession.interpretationTarget,
                        ),
                        asset = apiSession.asset.toTimetableAsset(),
                        speakers = apiSession.speakers
                            .map { speakerIdToSpeaker[it]!! }
                            .toPersistentList(),
                        levels = apiSession.levels.toPersistentList(),
                    )
                }
            }
                .sortedWith(
                    compareBy<TimetableItem> { it.startsAt }
                        .thenBy { it.room.sort }
                )
                .toPersistentList()
        )
    )
}

private fun LocaledResponse.toMultiLangText() = MultiLangText(jaTitle = ja, enTitle = en)
private fun SessionMessageResponse.toMultiLangText() = MultiLangText(jaTitle = ja, enTitle = en)
private fun SessionAssetResponse.toTimetableAsset() = TimetableAsset(videoUrl, slideUrl)
