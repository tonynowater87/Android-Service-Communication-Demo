package com.tonynowater.hyenatest.app.initializer

import android.content.Context
import androidx.startup.Initializer
import com.tonynowater.hyenatest.BuildConfig
import timber.log.Timber

class TimberInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.d("[DEBUG] TimberInitializer onCreate")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}