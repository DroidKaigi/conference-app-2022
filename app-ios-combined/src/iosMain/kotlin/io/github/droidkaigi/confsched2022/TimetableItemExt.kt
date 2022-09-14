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