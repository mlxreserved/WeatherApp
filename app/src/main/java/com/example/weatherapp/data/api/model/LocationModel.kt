package com.example.weatherapp.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    var name: String,
    val region: String,
    val country: String,
    val lat: Float,
    val lon: Float,
    @SerialName(value = "tz_id")
    val tzId: String,
    @SerialName(value = "localtime_epoch")
    val localtimeEpoch: Int,
    val localtime: String
)