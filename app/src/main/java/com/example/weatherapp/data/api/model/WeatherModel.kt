package com.example.weatherapp.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Weather (
    @SerialName(value = "location")
    val location: Location,
    @SerialName(value = "current")
    val current: Current
)