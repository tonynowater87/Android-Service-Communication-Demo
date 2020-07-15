package com.tonynowater.hyenatest.app.service.bike

import io.reactivex.rxjava3.subjects.PublishSubject

interface Bike {
    var isConnected: Boolean
    fun getBikeConnectedEvents(): PublishSubject<BikeEvents>
    fun connect()
    fun disconnect()
}