package io.github.droidkaigi.confsched2022.ui

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
