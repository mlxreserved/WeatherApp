package com.example.weatherapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class City(
    @PrimaryKey
    val coordinate: String,
    val name: String,
    val date: Long
)
