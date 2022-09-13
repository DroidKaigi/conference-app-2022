package io.github.droidkaigi.confsched2022

import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItem.Session
import io.github.droidkaigi.confsched2022.model.TimetableItem.Special
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

// workaround for type-casting of sealed class TimetableItem
fun TimetableItem.asSession(): Session? = this as? Session

fun TimetableItem.asSpecial(): Special? = this as? Special

fun TimetableItem.durationInMinutes(): Long = (this.endsAt - this.startsAt).toComponents { minutes, _, _ -> minutes }

fun TimetableItem.durationString(isJapanese: Boolean): String {
    val sessionStartDateTime = this.startsAt
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val sessionEndDateTime = this.endsAt
        .toLocalDateTime(TimeZone.currentSystemDefault())

    fun LocalDateTime.toTime() = "$hour:${minute.toString().padStart(2, '0')}"

    val month = if (isJapanese) {
        "${sessionStartDateTime.monthNumber}月"
    } else {
        sessionStartDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
    }

    val day = if (isJapanese) {
        "${sessionStartDateTime.dayOfMonth}日"
    } else {
        "${sessionStartDateTime.dayOfMonth}th"
    }

    return "$month $day ${sessionStartDateTime.toTime()}-${sessionEndDateTime.toTime()}"
}