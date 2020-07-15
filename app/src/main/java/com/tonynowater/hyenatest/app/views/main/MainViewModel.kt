package com.tonynowater.hyenatest.app.views.main

import android.app.Application
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.tonynowater.hyenatest.app.service.BikeConnectArguments
import com.tonynowater.hyenatest.app.service.BikeService
import com.tonynowater.hyenatest.app.service.bike.BikeEvents
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

class MainViewModel(private val appContext: Application) : ViewModel(), LifecycleObserver {

    val loading = MutableLiveData(false)
    val connected = MutableLiveData(false)
    val enableForeground = MutableLiveData(false)
    val speedRate = MutableLiveData<String>()

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun listenEvents(events: PublishSubject<BikeEvents>) {
        events
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Timber.d("[DEBUG] [BikeStatus] -> $it")
                when (it) {
                    is BikeEvents.Connected -> {
                        connected.postValue(true)
                        loading.postValue(false)
                    }
                    is BikeEvents.Disconnected -> {
                        connected.postValue(false)
                        speedRate.postValue("")
                        loading.postValue(false)
                    }
                    is BikeEvents.Connecting -> {
                        loading.postValue(true)
                    }
                    is BikeEvents.CurrentSpeed -> {
                        speedRate.postValue(it.speed.toString())
                    }
                    is BikeEvents.Error -> {
                        loading.postValue(false)
                    }
                }
            }
            .also { compositeDisposable.add(it) }
    }

    fun connect() {
        appContext.startService(getIntentToService(connect = true))
    }

    fun disconnect() {
        appContext.startService(getIntentToService(connect = false))
    }

    fun onCheckedChanged(checked: Boolean) {
        appContext.startService(getIntentToService(enableForeground = checked))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stopService() {
        // stop service when main activity destroyed
        appContext.startService(getIntentToService(stopService = true))
    }

    private fun getIntentToService(
        connect: Boolean? = null,
        enableForeground: Boolean? = null,
        stopService: Boolean? = null
    ): Intent {
        return Intent(appContext, BikeService::class.java).apply {
            putExtra(
                BikeService.EXTRA_ARGUMENT,
                BikeConnectArguments(connect, enableForeground, stopService)
            )
        }
    }
}