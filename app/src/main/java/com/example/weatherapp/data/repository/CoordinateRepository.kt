package com.example.weatherapp.data.repository

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.api.CoordinateService


private const val KEY_COORDINATE = BuildConfig.KEY_COORDINATE

class CoordinateRepository {

    suspend fun getCoordinate(city: String, lang: String): Pair<String, String> {
        val apiService = CoordinateService.create()

        val res = apiService.getCoordinate(
            geocode = city,
            apiKey = KEY_COORDINATE,
            lang = lang
        )

        val position =
            res.response.GeoObjectCollection.featureMember[0].GeoObject.Point.pos.split(" ")
                .reversed().joinToString(",")
        val cityName = res.response.GeoObjectCollection.featureMember[0].GeoObject.name
        return Pair(position, cityName)
    }

    suspend fun getMultiCoordinate(city: String, lang: String): List<String>{
        val apiService = CoordinateService.create()

        val res = apiService.getCoordinate(
            geocode = city,
            apiKey = KEY_COORDINATE,
            lang = lang
        )

        val listOfCoordinates = mutableListOf<String>()
        for(i in res.response.GeoObjectCollection.featureMember){
            var cityName = i.GeoObject.metaDataProperty.GeocoderMetaData.text
            val cityNameItems = cityName.split(", ").toMutableList()
            if(cityNameItems.size > 1) {
                cityNameItems.removeFirst()
            }
            cityName = cityNameItems.joinToString(", ")
            listOfCoordinates.add(cityName)
        }

        return listOfCoordinates
    }
}