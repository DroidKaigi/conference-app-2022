package io.github.droidkaigi.confsched2022.feature.sessions

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.strings.Strings
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.time.DurationUnit.MINUTES
import kotlin.time.toDuration

class SessionAlarm @Inject constructor(private val app: Application) {

    fun toggleRegister(session: TimetableItem, nextIsFavorite: Boolean) {
        if (nextIsFavorite) {
            register(session)
        } else {
            unregister(session)
        }
    }

    private fun register(session: TimetableItem) {
       val time = session.startsAt.minus(NOTIFICATION_TIME_BEFORE_START_MITES.toDuration(
           MINUTES
       ))
           .toEpochMilliseconds()

       if (System.currentTimeMillis() < time) {
        val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            AlarmManagerCompat.setAndAllowWhileIdle(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                time,
                createAlarmIntent(session)
            )
        }
    }

    private fun unregister(session: TimetableItem) {
        val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(createAlarmIntent(session))
    }

    private fun createAlarmIntent(timetableItem: TimetableItem): PendingIntent {
        val dtf = DateTimeFormatter.ofPattern("HH:mm")

        val displaySTime = timetableItem
            .startsAt
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .toJavaLocalDateTime()
            .format(dtf)

        val displayETime =timetableItem
            .endsAt
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .toJavaLocalDateTime()
            .format(dtf)

        val sessionTitle = app.getString(
            Strings.notification_message_session_title.resourceId,
            timetableItem.title.currentLangTitle)

        val sessionStartTime = app.getString(
            Strings.notification_message_session_start_time.resourceId,
            displaySTime,
            displayETime,
            timetableItem.room.name.currentLangTitle
        )

        val title: String
        val text: String
        // If you make this notification under Android N, the time and location will not be displayed.
        // So, under Android N, the session title is displayed in the title, the time and location are displayed in the text.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            title = app.getString(Strings.notification_title_session_start.resourceId)
            text = sessionTitle + "\n" + sessionStartTime
        } else {
            title = sessionTitle
            text = sessionStartTime
        }

        val intent = createNotificationIntentForSessionStart(
            app,
            timetableItem.id,
            title,
            text
        )

        return PendingIntent.getBroadcast(
            app,
            timetableItem.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createNotificationIntentForSessionStart(
        context: Context,
        sessionId: TimetableItemId,
        title: String,
        text: String
    ): Intent {
        return Intent(
            context,
            Class.forName(BROADCAST_RECEIVER_CLASS_NAME)
        ).apply {
            action =
                ACTION_FAVORITED_SESSION_START
            putExtra(EXTRA_SESSION_ID, sessionId.value)
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_TEXT, text)
            putExtra(EXTRA_CHANNEL_ID, NOTIFICATION_CHANNEL_ID)
        }
    }

    companion object {
        private const val NOTIFICATION_TIME_BEFORE_START_MITES = 10
        private const val BROADCAST_RECEIVER_CLASS_NAME = "io.github.droidkaigi.confsched2022.notification.AndroidBroadcastReceiver"
        const val ACTION_FAVORITED_SESSION_START = "ACTION_FAVORITED_SESSION_START"
        const val EXTRA_SESSION_ID = "EXTRA_SESSION_ID"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_TEXT = "EXTRA_TEXT"
        const val EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"
        const val NOTIFICATION_CHANNEL_ID = "favorite_session_start_channel"
    }
}
