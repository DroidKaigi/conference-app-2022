package io.github.droidkaigi.confsched2022.impl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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

        NotificationUtil.showNotification(context, title, text, channelId)
    }
}
