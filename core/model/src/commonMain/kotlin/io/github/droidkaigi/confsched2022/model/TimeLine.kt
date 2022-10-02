package io.github.droidkaigi.confsched2022.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Immutable
public data class TimeLine(
    private val instant: Instant,
    private val day: DroidKaigi2022Day,
) {

    public fun durationFromScheduleStart(targetDay: DroidKaigi2022Day): Duration? {
        if (day != targetDay) {
            return null
        }
        val currentTimeSecondOfDay =
            instant.toLocalDateTime(TimeZone.currentSystemDefault()).time.toSecondOfDay()
        val scheduleStartTimeSecondOfDay = LocalTime(hour = 10, minute = 0).toSecondOfDay()
        return ((currentTimeSecondOfDay - scheduleStartTimeSecondOfDay) / 60).minutes
    }

    public companion object {
        public fun now(): TimeLine? {
            val time = Clock.System.now()
            val day = DroidKaigi2022Day.ofOrNull(time)
            return day?.let { TimeLine(time, day) }
        }
    }
}
