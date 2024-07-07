package com.example.weatherapp.data.repository

import com.example.weatherapp.data.api.WeatherService
import com.example.weatherapp.data.api.model.Weather

private const val KEY_WEATHER = "e60480c2322946229e0112431240307"

class WeatherRepository {

    suspend fun getWeather(city: String, language: String): Weather {
        val apiService = WeatherService.create()

        return apiService.getWeather(city = city, lang = language, key = KEY_WEATHER)

    }
}