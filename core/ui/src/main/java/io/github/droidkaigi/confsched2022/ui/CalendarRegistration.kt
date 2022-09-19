package io.github.droidkaigi.confsched2022.ui

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Platform calendar registration feature wrapper
 */
interface CalendarRegistration {

    /**
     * Call platform calendar registration feature
     */
    fun register(
        startsAtMilliSeconds: Long,
        endsAtMilliSeconds: Long,
        title: String,
        location: String,
    )
}

val LocalCalendarRegistration = staticCompositionLocalOf<CalendarRegistration> {
    error("CompositionLocal LocalCalendarRegistration not present")
}
