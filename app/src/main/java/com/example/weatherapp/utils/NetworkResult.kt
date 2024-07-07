package com.example.weatherapp.utils

import com.example.weatherapp.data.api.model.Coordinate
import com.example.weatherapp.data.api.model.Weather

sealed interface WeatherResult {
    data class  Error(val message: String): WeatherResult
    data class Success(val data: Weather) : WeatherResult
    object Loading : WeatherResult
}

