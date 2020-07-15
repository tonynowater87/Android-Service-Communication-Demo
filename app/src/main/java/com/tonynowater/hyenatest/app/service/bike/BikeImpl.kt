package com.tonynowater.hyenatest.app.service.bike

import com.tonynowater.hyenatest.app.service.notification.NotificationHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class BikeImpl(private val notificationHelper: NotificationHelper) : Bike {

    private val bikeEvents: PublishSubject<BikeEvents> = PublishSubject.create()
    private val compositeDisposable = CompositeDisposable()
    override var isConnected: Boolean = false

    override fun getBikeConnectedEvents(): PublishSubject<BikeEvents> = bikeEvents

    override fun connect() {
        Observable
            .just(BikeEvents.Connected)
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { bikeEvents.onNext(BikeEvents.Connecting) }
            .subscribe {
                isConnected = true
                notificationHelper.updateNotification(connected = true, speed = null)
                bikeEvents.onNext(it)
                if (compositeDisposable.size() == 0) {
                    startRiding()
                }
            }
    }

    override fun disconnect() {
        Observable
            .just(BikeEvents.Disconnected)
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { bikeEvents.onNext(BikeEvents.Connecting) }
            .subscribe {
                isConnected = false
                notificationHelper.updateNotification(connected = false, speed = null)
                compositeDisposable.clear()
                bikeEvents.onNext(it)
            }
    }

    private fun startRiding() {

        Observable
            .interval(0L, 1L, TimeUnit.SECONDS)
            .flatMap {
                Observable.just(Random.nextInt(from = 0, until = 46))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                notificationHelper.updateNotification(connected = true, speed = it)
                bikeEvents.onNext(BikeEvents.CurrentSpeed(it))
            }
            .also { compositeDisposable.add(it) }
    }
}
