package io.github.droidkaigi.confsched2022.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

enum class DroidKaigi2022Day(val start: Instant, val end: Instant) {
    Day1(
        start = LocalDateTime
            .parse("2022-10-05T00:00:00")
            .toInstant(TimeZone.of("UTC+9")),
        end = LocalDateTime
            .parse("2021-10-06T00:00:00")
            .toInstant(TimeZone.of("UTC+9"))
    ),
    Day2(
        start = LocalDateTime
            .parse("2021-10-06T00:00:00")
            .toInstant(TimeZone.of("UTC+9")),
        end = LocalDateTime
            .parse("2021-10-07T00:00:00")
            .toInstant(TimeZone.of("UTC+9"))
    ),
    Day3(
        start = LocalDateTime
            .parse("2021-10-07T00:00:00")
            .toInstant(TimeZone.of("UTC+9")),
        end = LocalDateTime
            .parse("2021-10-08T00:00:00")
            .toInstant(TimeZone.of("UTC+9"))
    );

    companion object {
        fun ofOrNull(time: Instant): DroidKaigi2022Day? {
            return values().firstOrNull() {
                time in it.start..it.end
            }
        }
    }
}
