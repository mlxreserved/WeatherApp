package com.example.weatherapp.data.api

import com.example.weatherapp.data.api.model.Weather
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.weatherapi.com/v1/"

interface WeatherService {

    // Forecast return current and forecast

    @GET("forecast.json")
    suspend fun getWeather(@Query("q") city: String,
                           @Query("lang") lang: String,
                           @Query("key") key: String,
                           @Query("days") days: String): Weather

}
