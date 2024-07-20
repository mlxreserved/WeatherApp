package com.example.weatherapp.data.api

import com.example.weatherapp.data.api.model.Weather
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.weatherapi.com/v1/"

interface WeatherService {

    @GET("forecast.json")
    suspend fun getWeather(@Query("q") city: String,
                           @Query("lang") lang: String,
                           @Query("key") key: String,
                           @Query("days") days: String): Weather



    companion object{
        fun create(): WeatherService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()

            return retrofit.create(WeatherService::class.java)
        }
    }
}
