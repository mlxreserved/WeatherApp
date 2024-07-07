package com.example.weatherapp.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Current(
    @SerialName(value = "last_updated_epoch")
    val lastUpdatedEpoch: Int,
    @SerialName(value = "last_updated")
    val lastUpdate: String,
    @SerialName(value = "temp_c")
    val tempCelsius: Float,
    @SerialName(value = "temp_f")
    val tempFar: Float,
    @SerialName(value = "is_day")
    val isDay: Int,
    val condition: Condition,
    @SerialName(value = "wind_mph")
    val windMph: Float,
    @SerialName(value = "wind_kph")
    val windKph: Float,
    @SerialName(value = "wind_degree")
    val windDegree: Int,
    @SerialName(value = "wind_dir")
    val windDir: String,
    @SerialName(value = "pressure_mb")
    val pressureMb: Float,
    @SerialName(value = "pressure_in")
    val pressureIn: Float,
    @SerialName(value = "precip_mm")
    val precipMm: Float,
    @SerialName(value = "precip_in")
    val precipIn: Float,
    val humidity: Int,
    val cloud: Int,
    @SerialName(value = "feelslike_c")
    val feelsLikeCelsius: Float,
    @SerialName(value = "feelslike_f")
    val feelsLikeFar: Float,
    @SerialName(value = "windchill_c")
    val windchillCelsius: Float,
    @SerialName(value = "windchill_f")
    val windchillFar: Float,
    @SerialName(value = "heatindex_c")
    val heatindexCelsius: Float,
    @SerialName(value = "heatindex_f")
    val heatindexFar: Float,
    @SerialName(value = "dewpoint_c")
    val dewpointCelsius: Float,
    @SerialName(value = "dewpoint_f")
    val dewpointFar: Float,
    @SerialName(value = "vis_km")
    val visKm: Float,
    @SerialName(value = "vis_miles")
    val visMiles: Float,
    val uv: Float,
    @SerialName(value = "gust_mph")
    val gustMph: Float,
    @SerialName(value = "gust_kph")
    val gustKph: Float
)