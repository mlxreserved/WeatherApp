package com.example.weatherapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "cities")
data class City(
    @PrimaryKey
    val coordinate: String,
    val name: String,
    val date: Long
)
