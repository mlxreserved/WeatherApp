package com.example.weatherapp.data.api

import com.example.weatherapp.data.api.model.Coordinate
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://geocode-maps.yandex.ru/"

interface CoordinateService {
    @GET("1.x/")
    suspend fun getCoordinate(
        @Query("geocode") geocode: String,
        @Query("apikey") apiKey: String,
        @Query("lang") lang: String,
        @Query("format") format: String = "json",
    ): Coordinate

    companion object{
        fun create(): CoordinateService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(Json{ ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()))
                .build()

            return retrofit.create(CoordinateService::class.java)
        }
    }
}