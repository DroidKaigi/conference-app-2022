package io.github.droidkaigi.confsched2022.impl

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import androidx.core.os.bundleOf
import io.github.droidkaigi.confsched2022.ui.CalendarRegistration

class AndroidCalendarRegistration(private val context: Context) : CalendarRegistration {
    override fun register(
        startsAtMilliSeconds: Long,
        endsAtMilliSeconds: Long,
        title: String,
        location: String
    ) {
        val calendarIntent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtras(
                bundleOf(
                    CalendarContract.EXTRA_EVENT_BEGIN_TIME to startsAtMilliSeconds,
                    CalendarContract.EXTRA_EVENT_END_TIME to endsAtMilliSeconds,
                    CalendarContract.Events.TITLE to "[$location] $title",
                    CalendarContract.Events.EVENT_LOCATION to location
                )
            )
        }

        try {
            context.startActivity(calendarIntent)
        } catch (e: ActivityNotFoundException) {
            Log.e("AndroidCalendarRegistration", "Fail startActivity", e)
        }
    }
}
