package com.tonynowater.hyenatest.app.service.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.tonynowater.hyenatest.R
import com.tonynowater.hyenatest.app.views.main.MainActivity
import com.tonynowater.hyenatest.app.service.BikeConnectArguments
import com.tonynowater.hyenatest.app.service.BikeService
import timber.log.Timber

class NotificationHelperImpl(
    private val appContext: Context,
    private val notificationManager: NotificationManager
) : NotificationHelper {

    companion object {

        private const val groupId = "Hyena-Test-GroupId"
        private const val groupName = "Hyena-Test-GroupName"

        private const val normalChannelId = "Hyena-Test-Normal-ChannelId"
        private const val channelName = "Hyena-Test-ChannelName"
        private const val channelDescription = "Hyena-Test-ChannelDescription"
    }

    override var enableForeground: Boolean = false

    override fun getNotification(connected: Boolean?, speed: Int?): Notification {

        createNotificationGroupAndChannel()

        return getNotificationBuilder(connected, speed)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(appContext.getString(R.string.app_name))
            .setContentText(appContext.getString(R.string.foregroundSwitch))
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .addAction(
                buildNotificationAction(
                    actionText = appContext.getString(R.string.connect),
                    connect = true
                )
            )
            .addAction(
                buildNotificationAction(
                    actionText = appContext.getString(R.string.disconnect),
                    connect = false
                )
            )
            .setContentIntent(getMainActivityPendingIntent())
            .build()
    }

    private fun getMainActivityPendingIntent(): PendingIntent {
        return PendingIntent.getActivity(
            appContext,
            200,
            Intent(appContext, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun buildNotificationAction(
        actionText: String,
        connect: Boolean
    ): NotificationCompat.Action {
        return NotificationCompat.Action
            .Builder(
                null,
                actionText,
                PendingIntent.getService(
                    appContext,
                    100 + connect.hashCode(),
                    Intent(appContext, BikeService::class.java).apply {
                        putExtra(
                            BikeService.EXTRA_ARGUMENT,
                            BikeConnectArguments(connect = connect)
                        )
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .build()
    }

    private fun getNotificationBuilder(
        connected: Boolean?,
        speed: Int?
    ): NotificationCompat.Builder {

        val remoteViews = RemoteViews(appContext.packageName, R.layout.view_foreground_notification)

        remoteViews.setTextViewText(
            R.id.tv_notification_connect_status,
            appContext.getString(
                R.string.notification_status,
                connected?.let {
                    if (it) appContext.getString(R.string.connect) else appContext.getString(R.string.disconnect)
                } ?: "")
        )

        remoteViews.setTextViewText(
            R.id.tv_notification_speed_status,
            appContext.getString(R.string.notification_speed, speed ?: 0)
        )

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat
                .Builder(appContext, normalChannelId)
                .setCustomContentView(remoteViews)
        } else {
            NotificationCompat
                .Builder(appContext)
                .setContent(remoteViews)
        }
    }

    override fun updateNotification(connected: Boolean, speed: Int?) {
        if (enableForeground) {
            Timber.d("[DEBUG] [updateNotification] $connected, $speed")
            notificationManager.notify(
                BikeService.NOTIFICATION_ID,
                getNotification(connected, speed)
            )
        }
    }

    private fun createNotificationGroupAndChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            notificationManager.getNotificationChannel(normalChannelId) == null
        ) {

            Timber.d("[DEBUG] [createNotificationGroupAndChannel]")

            notificationManager.createNotificationChannelGroup(
                NotificationChannelGroup(
                    groupId,
                    groupName
                )
            )

            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(normalChannelId, channelName, importance)
            channel.description = channelDescription
            channel.enableVibration(false)

            notificationManager.createNotificationChannel(channel)
        }
    }
}