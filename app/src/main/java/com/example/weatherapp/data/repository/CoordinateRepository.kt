package com.example.weatherapp.data.repository

import com.example.weatherapp.data.api.CoordinateService


private const val KEY_COORDINATE = "8868527d-80b8-43d6-bab9-f6111ec94ee8"

class CoordinateRepository {

    suspend fun getCoordinate(city: String): Pair<String, String> {
        val apiService = CoordinateService.create()

        val res = apiService.getCoordinate(
            geocode = city,
            apiKey = KEY_COORDINATE,
            lang = "ru-RU"
        )

        val position =
            res.response.GeoObjectCollection.featureMember[0].GeoObject.Point.pos.split(" ")
                .reversed().joinToString(",")
        val cityName = res.response.GeoObjectCollection.featureMember[0].GeoObject.name
        return Pair(position, cityName)
    }
}