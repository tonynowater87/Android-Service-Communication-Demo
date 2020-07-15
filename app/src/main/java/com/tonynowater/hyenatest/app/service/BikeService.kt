package com.tonynowater.hyenatest.app.service

import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import android.os.Binder
import android.os.IBinder
import com.tonynowater.hyenatest.app.service.bike.Bike
import com.tonynowater.hyenatest.app.service.notification.NotificationHelper
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent
import timber.log.Timber

class BikeService : Service(), KoinComponent {

    companion object {
        const val EXTRA_ARGUMENT = "EXTRA_ARGUMENT"
        const val NOTIFICATION_ID: Int = 55660
    }

    private val bike by inject<Bike>()
    private val notificationHelper by inject<NotificationHelper>()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        Timber.d("[DEBUG] [onStartCommand]")

        (intent.extras?.getParcelable(EXTRA_ARGUMENT) as? BikeConnectArguments)?.also { args ->

            Timber.d("[DEBUG] [onStartCommand] [$args]")

            args.stopService?.also {
                if (!notificationHelper.enableForeground && it) {
                    stopSelf()
                }
            }

            args.enableForeground?.also {
                notificationHelper.enableForeground = it
                if (it) {
                    startForeground(
                        NOTIFICATION_ID,
                        notificationHelper.getNotification()
                    )
                } else {
                    stopForeground(true)
                }
            }

            args.connect?.also {
                if (it) {
                    bike.connect()
                } else {
                    bike.disconnect()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder = BikeBinder()

    inner class BikeBinder : Binder() {
        fun isConnected() = bike.isConnected
        fun enableForeground() = notificationHelper.enableForeground
        fun getBikeConnectedEvents() = bike.getBikeConnectedEvents()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("[DEBUG] [onDestroy]")
        bike.disconnect()
    }
}

