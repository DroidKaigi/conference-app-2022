package io.github.droidkaigi.confsched2022.feature.sessions

import io.github.droidkaigi.confsched2022.model.MultiLangText
import io.github.droidkaigi.confsched2022.model.TimetableAsset
import io.github.droidkaigi.confsched2022.model.TimetableCategory
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItem.Session
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.model.TimetableRoom
import io.github.droidkaigi.confsched2022.model.TimetableSpeaker
import io.github.droidkaigi.confsched2022.ui.Result
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant


data class TimeTableDetailUiModel(
    val timetableDetailState: TimetableDetailState,
) {
    sealed interface TimetableDetailState {

        data class Loaded(val timetableItem: TimetableItem) : TimetableDetailState

        object Loading : TimetableDetailState

        companion object {
            fun of(timetableItemResult: Result<TimetableItem>): TimetableDetailState {
                return when (timetableItemResult) {
                    Result.Loading -> {
                        Loading
                    }
                    is Result.Success -> {
                        Loaded(timetableItemResult.data)
                    }
                    else -> {
                        // TODO
                        // SessionsState.Error
                        Loading
                    }
                }
            }
        }
    }
}

fun fakeTimetableDetail() =  Session(
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
    room = TimetableRoom(
        id = 2,
        name = MultiLangText("Room1", "Room2"),
        sort = 1
    ),
    targetAudience = "For App developer アプリ開発者向け",
    language = "JAPANESE",
    asset = TimetableAsset(
        videoUrl = "https://www.youtube.com/watch?v=hFdKCyJ-Z9A",
        slideUrl = "https://droidkaigi.jp/2021/",
    ),
    speakers = listOf(
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
    ).toPersistentList(),
    description = "これはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。",
    message = null,
    levels = listOf(
        "INTERMEDIATE",
    ).toPersistentList(),
)

