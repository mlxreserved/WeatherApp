package com.example.weatherapp.data.repository

import com.example.weatherapp.data.database.City
import com.example.weatherapp.data.database.CityDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineCitiesRepository @Inject constructor(private val cityDao: CityDao): CitiesRepository {
    override fun delete() = cityDao.delete()

    override fun getAllCities(): Flow<List<City>> = cityDao.getAllCities()

    override fun insert(city: City) = cityDao.insert(city)
}