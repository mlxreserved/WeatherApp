package com.example.weatherapp.data.api

import com.example.weatherapp.data.api.model.Coordinate
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://geocode-maps.yandex.ru/"

interface CoordinateService {


    // return coordinate
    @GET("1.x/")
    suspend fun getCoordinate(
        @Query("geocode") geocode: String,
        @Query("apikey") apiKey: String,
        @Query("lang") lang: String,
        @Query("format") format: String = "json"
    ): Coordinate

}