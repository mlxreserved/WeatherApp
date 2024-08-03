package com.example.weatherapp.data.repository

import com.example.weatherapp.data.City
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {
    fun getAllCities(): Flow<List<City>>

    fun delete()

    fun insert(city: City)
}