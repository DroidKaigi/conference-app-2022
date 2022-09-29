package io.github.droidkaigi.confsched2022.impl

import android.Manifest.permission
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationUtil {

    fun showNotification(
        context: Context,
        title: String,
        text: String,
        channelId: String
    ) {
        val notificationBuilder = notificationBuilder(
            context,
            channelId
        ).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                showBundleNotification(
                    context,
                    title,
                    channelId
                )
                setGroup(channelId)
            } else {
                setContentTitle(title)
            }
            setContentText(text)
            setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(text)
            )
            setAutoCancel(true)
            setSmallIcon(io.github.droidkaigi.confsched2022.core.designsystem.R.drawable.ic_app)
        }

        val notificationManagerCompat = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManagerCompat.notify(text.hashCode(), notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showBundleNotification(context: Context, title: String, channelId: String) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val notification = notificationBuilder(
            context,
            channelId
        )
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setSummaryText(title)
            )
            .setSmallIcon( io.github.droidkaigi.confsched2022.core.designsystem.R.drawable.ic_app )
            .setGroup(channelId)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(channelId.hashCode(), notification)
    }

    private fun notificationBuilder(
        context: Context,
        channelId: String
    ): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createDefaultNotificationChannel(
                context,
                NotificationChannelInfo.of(
                    channelId
                )
            )
        }
        val builder = NotificationCompat.Builder(context, channelId)
        builder.setChannelId(channelId)
        return builder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDefaultNotificationChannel(
        context: Context,
        notificationChannelInfo: NotificationChannelInfo
    ) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            notificationChannelInfo.channelId,
            notificationChannelInfo.channelName(context),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }
}

enum class NotificationChannelInfo(
    val channelId: String,
    private val channelNameResId: Int,
    val defaultLaunchUrl: String
) {
    DEFAULT(
        "default_channel",
        0,
        "https://droidkaigi.jp/2022"
    ),
    FAVORITE_SESSION_START(
        "favorite_session_start_channel",
        1,
        "https://droidkaigi.jp/2022"
    );

//    fun channelName(context: Context): String = context.getString(channelNameResId)
    fun channelName(context: Context): String = "torikatsutest"

    companion object {
        fun of(channelId: String): NotificationChannelInfo {
            return values().find {
                it.channelId == channelId
            } ?: DEFAULT
        }
    }
}
