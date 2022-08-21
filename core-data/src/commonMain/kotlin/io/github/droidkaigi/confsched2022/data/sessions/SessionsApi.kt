package io.github.droidkaigi.confsched2022.data.sessions

import io.github.droidkaigi.confsched2022.data.sessions.response.LocaledResponse
import io.github.droidkaigi.confsched2022.data.sessions.response.SessionAllResponse
import io.github.droidkaigi.confsched2022.data.sessions.response.SessionAssetResponse
import io.github.droidkaigi.confsched2022.data.sessions.response.SessionMessageResponse
import io.github.droidkaigi.confsched2022.model.MultiLangText
import io.github.droidkaigi.confsched2022.model.Timetable
import io.github.droidkaigi.confsched2022.model.TimetableAsset
import io.github.droidkaigi.confsched2022.model.TimetableCategory
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.TimetableItemList
import io.github.droidkaigi.confsched2022.model.TimetableRoom
import io.github.droidkaigi.confsched2022.model.TimetableSpeaker
import io.ktor.client.HttpClient
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class SessionsApi(private val httpClient: HttpClient) {
    suspend fun timetable(): Timetable {
//        return httpClient.get("xxxxx").body()
        return TODO()
    }
}

private fun String.toInstantAsJST(): Instant {
    val (date, _) = split("+")
    return LocalDateTime.parse(date).toInstant(TimeZone.of("UTC+9"))
}

internal fun SessionAllResponse.toTimetable(): Timetable {
    val timetableContents = this
    val speakerIdToSpeaker: Map<String, TimetableSpeaker> = timetableContents.speakers
        .groupBy { it.id }
        .mapValues { (_, apiSpeakers) ->
            apiSpeakers.map { apiSpeaker ->
                TimetableSpeaker(
                    name = apiSpeaker.fullName,
                    bio = apiSpeaker.bio,
                    iconUrl = apiSpeaker.profilePicture,
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
                        language = apiSession.language,
                        asset = apiSession.asset.toTimetableAsset(),
                        description = apiSession.description,
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
                        language = apiSession.language,
                        asset = apiSession.asset.toTimetableAsset(),
                        speakers = apiSession.speakers
                            .map { speakerIdToSpeaker[it]!! }
                            .toPersistentList(),
                        levels = apiSession.levels.toPersistentList(),
                    )
                }
            }.toPersistentList()
        )
    )
}

private fun LocaledResponse.toMultiLangText() = MultiLangText(jaTitle = ja, enTitle = en)
private fun SessionMessageResponse.toMultiLangText() = MultiLangText(jaTitle = ja, enTitle = en)
private fun SessionAssetResponse.toTimetableAsset() = TimetableAsset(videoUrl, slideUrl)
