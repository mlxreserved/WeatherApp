package com.example.weatherapp.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.Network
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.api.WeatherService
import com.example.weatherapp.data.api.model.Hour
import com.example.weatherapp.data.api.model.Weather
import com.example.weatherapp.data.repository.CoordinateRepository
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.utils.WeatherResult
import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.LanguageDetector
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date

private const val WEATHER_TAG = "WEATHER"
private const val COORDINATE_TAG = "COORDINATE"

class WeatherViewModel: ViewModel() {

    // Результат погодного запроса
    var weatherUiState: WeatherResult by mutableStateOf(WeatherResult.Loading)
        private set

    var isLoaded by mutableStateOf(false)
        private set

    var coordinateList: List<String> by mutableStateOf(emptyList())
        private set

    var hourList = mutableListOf<Hour>()
        private set

    // Город, для которого ищется погода
    var textFieldCity by  mutableStateOf("")
        private set

    val languageMap = mapOf("ru" to "ru_RU", "en" to "en_US")

    // Язык, на котором отображается информация о погоде
    var lang by mutableStateOf("ru")
        private set

    fun getWeather(city: String){
        if (city.isNotEmpty()) {
            changeIsLoaded()
            viewModelScope.launch {
                val weatherRepository = WeatherRepository()
                weatherUiState = WeatherResult.Loading
                weatherUiState = try { // Попытка получить погоду
                    val (coordinateOfCity, nameCity) = getCoordinate(city)
                    val weather = weatherRepository.getWeather(coordinateOfCity, lang)
                    weather.location.name = nameCity
                    addHourToHourList(weather)
                    closeSearchScreen()
                    WeatherResult.Success(weather)
                } catch (e: IOException) {
                    Log.e(WEATHER_TAG, "${e.message}")
                    WeatherResult.Error("${e.message}")
                }
            }
        }
    }

    fun getMultiCoordinate(city: String) {
        getLanguage(city)
        viewModelScope.launch {
            try {
                val coordinateRepository = CoordinateRepository()
                coordinateList = coordinateRepository.getMultiCoordinate(city = city, languageMap[lang] ?: "ru_RU")
            } catch(e: IOException){
                Log.e(COORDINATE_TAG, "${e.message}")
            }
        }
    }

    // Получение координат города
    private suspend fun getCoordinate(city: String): Pair<String,String> {
        try {
            val coordinateRepository = CoordinateRepository()
            val coordinate: Pair<String, String> = coordinateRepository.getCoordinate(city = city, languageMap[lang] ?: "ru_RU")
            return coordinate
        } catch (e: IOException){
            Log.e(COORDINATE_TAG,"${e.message}")
            return Pair("","")
        }
    }

    // Изменение состояния поля ввода
    fun updateCity(input: String){
        textFieldCity = input
    }

    fun clearCity(){
        textFieldCity = ""
    }

    fun closeSearchScreen(){
        coordinateList = emptyList()
        clearCity()
    }

    private fun changeIsLoaded(){
        isLoaded = true
    }

    @SuppressLint("NewApi")
    fun formatDate(date: String, week: Boolean): String{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateFormatter = if(week) DateTimeFormatter.ofPattern("EEEE")
        else DateTimeFormatter.ofPattern("dd MMMM")

        val formatedDate = LocalDate.parse(date,formatter)
        return formatedDate.format(dateFormatter)

    }

    fun getLanguage(city: String){
        val detector: LanguageDetector = LanguageDetectorBuilder.fromLanguages(Language.ENGLISH, Language.RUSSIAN).build()
        val detectedLanguage: Language = detector.detectLanguageOf(text = city)
        lang = detectedLanguage.name.substring(0,2).lowercase()
    }

    @SuppressLint("NewApi")
    fun formatTime(time: String): String{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val formattedTime = LocalDateTime.parse(time,formatter)
        return formattedTime.format(timeFormatter)
    }

    @SuppressLint("NewApi")
    private fun addHourToHourList(weather: Weather){
        hourList.clear()
        for(i in LocalTime.now().hour..< weather.forecast.forecastday[0].hour.size) {
            hourList.add(weather.forecast.forecastday[0].hour[i])
        }
        for(i in 0..LocalTime.now().hour){
            hourList.add(weather.forecast.forecastday[1].hour[i])
        }
    }
}
