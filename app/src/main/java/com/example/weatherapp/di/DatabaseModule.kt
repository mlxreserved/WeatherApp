package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.data.database.CityDao
import com.example.weatherapp.data.database.CityDatabase
import com.example.weatherapp.data.repository.CitiesRepository
import com.example.weatherapp.data.repository.OfflineCitiesRepository
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
interface DatabaseComponent{
    val citiesRepository: CitiesRepository
}

@Module(includes = [DatabaseBindModule::class])
class DatabaseModule(private val context: Context){

    @Singleton
    @Provides
    fun provideCityDatabase(): CityDatabase {
        return CityDatabase.getDatabase(context)
    }

    @Provides
    fun provideCityDao(db: CityDatabase): CityDao {
        return db.cityDao()
    }
}

@Module
interface DatabaseBindModule{
    @Binds
    fun bindOfflineCitiesRepository_to_CitiesRepository(offlineCitiesRepository: OfflineCitiesRepository): CitiesRepository
}
