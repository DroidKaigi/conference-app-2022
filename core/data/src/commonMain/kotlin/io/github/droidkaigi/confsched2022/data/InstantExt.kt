package io.github.droidkaigi.confsched2022.data

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

internal fun String.toInstantAsJST(): Instant {
    val (date, _) = split("+")
    return LocalDateTime.parse(date).toInstant(TimeZone.of("UTC+9"))
}
