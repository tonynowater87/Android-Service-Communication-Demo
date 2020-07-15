# Android Service Communication Demo

簡單展示Activity、Service、Notification之間溝通的方式

## 注意事項
- Android 8.0 (26) 以上顯示Notification前要先建立NotificationGroup&Channel
```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
    notificationManager.getNotificationChannel(normalChannelId) == null
) {

    notificationManager.createNotificationChannelGroup(NotificationChannelGroup(groupId,groupName))

    val importance = NotificationManager.IMPORTANCE_LOW
    val channel = NotificationChannel(normalChannelId, channelName, importance)
    channel.description = channelDescription
    channel.enableVibration(false)

    notificationManager.createNotificationChannel(channel)
}
```

- Android 9.0 (28) 以上使用前景Service需要加上這個權限
```xml   
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```

## 截圖

<img src="https://github.com/tonynowater87/Android-Service-Communication-Demo/blob/master/screenshots/screenshot1.png" width="30%">

<img src="https://github.com/tonynowater87/Android-Service-Communication-Demo/blob/master/screenshots/screenshot2.png" width="30%">

## 參考資料
[BindService In Android](https://medium.com/@JamesQI/bindservice-in-android-82d1875f03ce)