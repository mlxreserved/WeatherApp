package com.example.weatherapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao{


    //@Query("INSERT INTO cities (name, coordinate, date) VALUES (:cityName, :cityCoordinate, :date)")
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: City)

    @Query("DELETE FROM cities WHERE date = (SELECT min(date) FROM cities) ")
    fun delete()

    @Query("SELECT * FROM cities ORDER BY date DESC")
    fun getAllCities(): Flow<List<City>>
}
