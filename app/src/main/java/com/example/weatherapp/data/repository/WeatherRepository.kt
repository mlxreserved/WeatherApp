package com.example.weatherapp.data.repository

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.api.WeatherService
import com.example.weatherapp.data.api.model.Weather

private const val KEY_WEATHER = BuildConfig.KEY_WEATHER

class WeatherRepository {

    suspend fun getWeather(city: String, language: String): Weather {
        val apiService = WeatherService.create()

        return apiService.getWeather(city = city, lang = language, key = KEY_WEATHER, days = "3")

    }
}