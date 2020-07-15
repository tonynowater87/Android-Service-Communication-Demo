package com.tonynowater.hyenatest.app.service

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BikeConnectArguments(
    val connect: Boolean? = null,
    val enableForeground: Boolean? = null,
    val stopService: Boolean? = null
) : Parcelable