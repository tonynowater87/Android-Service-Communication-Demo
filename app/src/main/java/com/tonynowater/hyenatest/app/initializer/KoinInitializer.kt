package com.tonynowater.hyenatest.app.initializer

import android.content.Context
import androidx.startup.Initializer
import com.tonynowater.hyenatest.app.mainModule
import com.tonynowater.hyenatest.app.serviceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class KoinInitializer : Initializer<Unit> {

    override fun create(appContext: Context) {
        startKoin {
            androidLogger()
            androidContext(appContext)
            modules(
                listOf(
                    mainModule,
                    serviceModule
                )
            )
        }
        Timber.d("[DEBUG] KoinInitializer onCreate")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(TimberInitializer::class.java)
}