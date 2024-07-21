package com.example.weatherapp.data.repository

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.api.WeatherService
import com.example.weatherapp.data.api.model.Weather
import dagger.Module
import dagger.Provides
import javax.inject.Inject

private const val KEY_WEATHER = BuildConfig.KEY_WEATHER

interface WeatherRepository{
    suspend fun getWeather(city: String, language: String): Weather
}

class WeatherRepositoryImpl @Inject constructor(private val weatherService: WeatherService): WeatherRepository{

    override suspend fun getWeather(city: String, language: String): Weather =
        weatherService.getWeather(city = city, lang = language, key = KEY_WEATHER, days = "3")

}