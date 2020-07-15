package com.tonynowater.hyenatest.app.service.bike

sealed class BikeEvents {
    object Connected : BikeEvents()
    object Disconnected : BikeEvents()
    object Connecting : BikeEvents()
    data class CurrentSpeed(val speed: Int) : BikeEvents()
    data class Error(val errorMsg: String) : BikeEvents()
}