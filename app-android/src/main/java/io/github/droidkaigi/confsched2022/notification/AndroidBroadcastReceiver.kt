package io.github.droidkaigi.confsched2022.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import io.github.droidkaigi.confsched2022.MainActivity
import io.github.droidkaigi.confsched2022.feature.sessions.SessionAlarm.Companion.EXTRA_CHANNEL_ID
import io.github.droidkaigi.confsched2022.feature.sessions.SessionAlarm.Companion.EXTRA_SESSION_ID
import io.github.droidkaigi.confsched2022.feature.sessions.SessionAlarm.Companion.EXTRA_TEXT
import io.github.droidkaigi.confsched2022.feature.sessions.SessionAlarm.Companion.EXTRA_TITLE

class AndroidBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return
        val sessionId = intent.getStringExtra(EXTRA_SESSION_ID) ?: return
        val title = intent.getStringExtra(EXTRA_TITLE) ?: return
        val text = intent.getStringExtra(EXTRA_TEXT) ?: return
        val channelId = intent.getStringExtra(EXTRA_CHANNEL_ID) ?: return
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            sessionId.toUri(),
            context,
            MainActivity::class.java
        )

        TaskStackBuilder.create(context).run {
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }

            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(0, flags)
        }?.let { deepLinkPendingIntent ->
            NotificationUtil.showNotification(
                context,
                title,
                text,
                channelId,
                deepLinkPendingIntent
            )
        }
    }
}
