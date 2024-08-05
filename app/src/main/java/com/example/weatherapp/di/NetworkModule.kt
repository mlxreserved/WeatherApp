package com.example.weatherapp.di

import com.example.weatherapp.data.api.CoordinateService
import com.example.weatherapp.data.api.WeatherService
import com.example.weatherapp.data.repository.CoordinateRepository
import com.example.weatherapp.data.repository.CoordinateRepositoryImpl
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create

private const val BASE_URL_COORDINATE = "https://geocode-maps.yandex.ru/"
private const val BASE_URL_WEATHER = "https://api.weatherapi.com/v1/"


@Component(modules = [AppModule::class])
interface AppComponent{
    val weatherRepository: WeatherRepository
    val coordinateRepository: CoordinateRepository

}

@Module(includes = [NetworkModule::class, AppBindModule::class])
class AppModule

@Module
class NetworkModule{

    @Provides
    fun provideWeatherService(): WeatherService{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER)
            .addConverterFactory(Json{ ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()))
            .build()

        return retrofit.create()
    }

    @Provides
    fun provideCoordinateService(): CoordinateService{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_COORDINATE)
            .addConverterFactory(Json{ ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()))
            .build()
        return retrofit.create()
    }
}


@Module
interface AppBindModule{

    @Binds
    fun bindWeatherRepositoryImpl_to_WeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    fun bindCoordinateRepositoryImpl_to_CoordinateRepository(
        coordinateRepositoryImpl: CoordinateRepositoryImpl
    ): CoordinateRepository
}

