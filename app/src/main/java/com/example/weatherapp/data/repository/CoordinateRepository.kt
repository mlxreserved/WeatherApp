package com.example.weatherapp.data.repository

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.api.CoordinateService
import com.example.weatherapp.data.api.model.Coordinate
import dagger.Module
import dagger.Provides
import javax.inject.Inject


private const val KEY_COORDINATE = BuildConfig.KEY_COORDINATE

interface CoordinateRepository{

    suspend fun getCoordinate(city: String, lang: String): Coordinate

}

class CoordinateRepositoryImpl @Inject constructor(private val coordinateService: CoordinateService): CoordinateRepository {

    override suspend fun getCoordinate(city: String, lang: String): Coordinate = coordinateService.getCoordinate(
        geocode = city,
        apiKey = KEY_COORDINATE,
        lang = lang
    )
}