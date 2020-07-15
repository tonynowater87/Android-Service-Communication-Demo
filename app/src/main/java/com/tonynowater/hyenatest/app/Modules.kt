package com.tonynowater.hyenatest.app

import android.app.NotificationManager
import android.content.Context
import com.tonynowater.hyenatest.app.views.main.MainViewModel
import com.tonynowater.hyenatest.app.service.bike.Bike
import com.tonynowater.hyenatest.app.service.bike.BikeImpl
import com.tonynowater.hyenatest.app.service.notification.NotificationHelper
import com.tonynowater.hyenatest.app.service.notification.NotificationHelperImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel { MainViewModel(appContext = get()) }
}

val serviceModule = module {

    single { androidApplication().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    single<NotificationHelper> {
        NotificationHelperImpl(
            appContext = get(),
            notificationManager = get()
        )
    }

    single<Bike> { BikeImpl(notificationHelper = get()) }
}