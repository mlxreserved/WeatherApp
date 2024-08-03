package com.example.weatherapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [City::class], version = 2, exportSchema = false)
abstract class CityDatabase: RoomDatabase() {

    abstract fun cityDao(): CityDao

    companion object{
        @Volatile
        private var Instance: CityDatabase? = null

        fun getDatabase(context: Context): CityDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CityDatabase::class.java, "city_database")
                .fallbackToDestructiveMigration()
                .build()
                .also{ Instance = it}}
        }
    }
}