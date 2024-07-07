package com.example.weatherapp.presentation.ui

import android.content.Context
import android.net.Network
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.api.WeatherService
import com.example.weatherapp.data.api.model.Weather
import com.example.weatherapp.data.repository.CoordinateRepository
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.utils.WeatherResult
import kotlinx.coroutines.launch
import java.io.IOException

private const val COORDINATE_TAG = "COORDINATE"
private const val WEATHER_TAG = "WEATHER"

class WeatherViewModel: ViewModel() {

    // Результат погодного запроса
    var weatherUiState: WeatherResult by mutableStateOf(WeatherResult.Loading)
        private set

    // Город, для которого ищется погода
    var textFieldCity by  mutableStateOf("")
        private set

    // Язык, на котором отображается информация о погоде
    var lang by mutableStateOf("ru_RU")
        private set

    fun getWeather(city: String,language: String){
        viewModelScope.launch{
            val weatherRepository = WeatherRepository()
            weatherUiState = WeatherResult.Loading
            weatherUiState = try{ // Попытка получить погоду
                val (coordinateOfCity, nameCity) = getCoordinate(city)
                val weather = weatherRepository.getWeather(coordinateOfCity,language)
                weather.location.name = nameCity
                WeatherResult.Success(weather)
            } catch (e: IOException) {
                Log.e(WEATHER_TAG, "${e.message}")
                WeatherResult.Error("${e.message}")
/*          try{ // Попытка получить погоду в городах, которые не распознаются
                    val (coordinateOfCity, nameCity) = getCoordinate(city)
                    val weather = weatherRepository.getWeather(coordinateOfCity,language)
                    weather.location.name = nameCity
                    WeatherResult.Success(weather)
                } catch (e: Exception) {
                    WeatherResult.Error("${e.message}")
                }
                */
            }

        }
    }

    // Получение координат города
    private suspend fun getCoordinate(city: String): Pair<String,String> {
        val coordinateRepository = CoordinateRepository()
        val coordinate: Pair<String,String> = coordinateRepository.getCoordinate(city = city) ?: Pair("","")
        return coordinate
    }

    // Изменение состояния поля ввода
    fun updateCity(input: String){
        textFieldCity = input
    }

    fun clearCity(){
        textFieldCity = ""
    }
}
