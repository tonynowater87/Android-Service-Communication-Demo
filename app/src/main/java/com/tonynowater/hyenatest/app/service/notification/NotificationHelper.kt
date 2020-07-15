package com.tonynowater.hyenatest.app.service.notification

import android.app.Notification

interface NotificationHelper {
    var enableForeground: Boolean
    fun getNotification(connected: Boolean? = null, speed: Int? = null): Notification
    fun updateNotification(connected: Boolean, speed: Int? = null)
}