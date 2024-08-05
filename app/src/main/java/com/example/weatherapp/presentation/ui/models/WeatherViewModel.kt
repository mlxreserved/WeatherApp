package com.example.weatherapp.presentation.ui.models

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weatherapp.data.City
import com.example.weatherapp.data.api.model.Coordinate
import com.example.weatherapp.data.api.model.Hour
import com.example.weatherapp.data.api.model.Weather
import com.example.weatherapp.data.repository.CitiesRepository
import com.example.weatherapp.data.repository.CoordinateRepository
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.di.MainApp
import com.example.weatherapp.utils.WeatherResult
import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.LanguageDetector
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private const val WEATHER_TAG = "WEATHER"
private const val COORDINATE_TAG = "COORDINATE"


data class WeatherAppUiState(
    val weatherUiState: WeatherResult = WeatherResult.Loading,
    val selectedItem: Int = -1,
    val currentTheme: Boolean? = null,
    val currentEnterDirection: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Right,
    val currentExitDirection: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Left,
    //val isLoaded: Boolean = false,
    val coordinateList: List<String> = emptyList(),
    val hourList: MutableList<Hour> = mutableListOf(),
    var textFieldCity: String = "",
    val languageMap: Map<String, String> = mapOf("ru" to "ru_RU", "en" to "en_US"),
    val lang: String = "ru"
    )

data class SearchUiState(
    val storyOfSearch: List<City> = listOf(),
    )


//
//data class CityState(
//    val id: Int = 0,
//    val name: String = "",
//    val coordinate: String = "",
//    val date: Long = 0L
//)
//
//fun CityState.toCity(): City = City(
//    id = id,
//    name = name,
//    coordinate = coordinate,
//    date = date
//)

class WeatherViewModel (
    private val weatherRepository: WeatherRepository,
    private val coordinateRepository: CoordinateRepository,
    private val citiesRepository: CitiesRepository,
): ViewModel() {
    private val _uiStateWeather = MutableStateFlow(WeatherAppUiState())
    val uiStateWeather: StateFlow<WeatherAppUiState> = _uiStateWeather.asStateFlow()

    private val _uiStateSearch = MutableStateFlow(SearchUiState())
    val uiStateSearch: StateFlow<SearchUiState> = citiesRepository.getAllCities().map { SearchUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS ),
            initialValue = SearchUiState()
        )

    init{
        getWeather("Москва", true)
    }

    fun getWeather(city: String, isReloading: Boolean){
        if (city.isNotBlank()) {
            getLanguage(city)
            viewModelScope.launch {
                _uiStateWeather.update { it.copy(weatherUiState = WeatherResult.Loading) }
                val res = try { // Попытка получить погоду
                    val (coordinateOfCity,nameCity, nameFullCity) = getCoordinate(city)
                    val weather =
                        weatherRepository.getWeather(coordinateOfCity, _uiStateWeather.value.lang)
                    weather.location.name = nameCity
                    if(!isReloading) {
                        saveCity(
                            City(
                                name = nameFullCity,
                                coordinate = coordinateOfCity,
                                date = getCurrentTime()
                            )
                        )
                    }
                    addHourToHourList(weather)
                    closeSearchScreen()
                    WeatherResult.Success(weather)

                } catch (e: Exception) {
                    Log.e(WEATHER_TAG, "${e.message}")
                    WeatherResult.Error("${e.message}")
                }
                _uiStateWeather.update {
                    it.copy(
                        weatherUiState = res
                    )
                }
            }
        }
    }


    @SuppressLint("NewApi")
    fun getCurrentTime(): Long{
        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
    }

    fun getMultiCoordinate(city: String) {
        getLanguage(city)
        viewModelScope.launch {
            try {
                val res = coordinateRepository.getCoordinate(city = city, _uiStateWeather.value.languageMap[_uiStateWeather.value.lang] ?: "ru_RU")
                _uiStateWeather.update { it.copy(coordinateList = handleNameOfCity(res)) }
            } catch(e: IOException){
                Log.e(COORDINATE_TAG, "${e.message}")
            }
        }
    }

    private fun handleNameOfCity(res: Coordinate): List<String>{
        val listOfCoordinates = mutableListOf<String>()
        for(i in res.response.GeoObjectCollection.featureMember){
            var cityName = i.GeoObject.metaDataProperty.GeocoderMetaData.text
            val cityNameItems = cityName.split(", ").toMutableList()
            if(cityNameItems.size > 1) {
                cityNameItems.removeFirst()
            }
            cityName = cityNameItems.joinToString(", ")
            listOfCoordinates.add(cityName)
        }
        return listOfCoordinates
    }

    // Получение координат города
    private suspend fun getCoordinate(city: String): Triple<String,String, String> {
        try {
            val coordinate: Triple<String, String, String> = convertCoordinate(coordinateRepository.getCoordinate(city = city, _uiStateWeather.value.languageMap[_uiStateWeather.value.lang] ?: "ru_RU"))
            return coordinate
        } catch (e: IOException){
            Log.e(COORDINATE_TAG,"${e.message}")
            return Triple("","","")
        }
    }

    // Изменение состояния поля ввода
    fun updateCity(input: String){
        _uiStateWeather.update { it.copy(textFieldCity = input) }

    }

    fun changeSelectedItem(item: Int){
        _uiStateWeather.update { it.copy(selectedItem = item) }
    }

    fun clearCity(){
        _uiStateWeather.update { it.copy(textFieldCity = "") }
    }

    private fun saveCity(city: City){
        viewModelScope.launch(Dispatchers.IO) {
            if(uiStateSearch.value.storyOfSearch.size==10){
                citiesRepository.delete()
            }
            citiesRepository.insert(city)
        }
    }


    fun closeSearchScreen(){
        _uiStateWeather.update { it.copy(coordinateList = emptyList()) }
        clearCity()
    }


    @SuppressLint("NewApi")
    fun formatDate(date: String, week: Boolean, short: Boolean): String{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateFormatter = if(week) {
            if(short){
                DateTimeFormatter.ofPattern("E")
            } else {
                DateTimeFormatter.ofPattern("EEEE")
            }
        }
        else {
            if(short){
                DateTimeFormatter.ofPattern("dd")
            } else {
                DateTimeFormatter.ofPattern("dd MMMM")
            }
        }

        val formatedDate = LocalDate.parse(date,formatter)
        return formatedDate.format(dateFormatter)

    }

    fun getLanguage(city: String){
        val detector: LanguageDetector = LanguageDetectorBuilder.fromLanguages(Language.ENGLISH, Language.RUSSIAN).build()
        val detectedLanguage: Language = detector.detectLanguageOf(text = city)
        _uiStateWeather.update { it.copy(lang = detectedLanguage.name.substring(0,2).lowercase()) }
    }

    @SuppressLint("NewApi")
    fun formatTime(time: String, isNextDay: Boolean): String{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        if(!isNextDay){
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val formattedTime = LocalDateTime.parse(time,formatter)
            return formattedTime.format(timeFormatter)
        } else {
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm d MMM")
            val formattedTime = LocalDateTime.parse(time,formatter)
            return formattedTime.format(timeFormatter)
        }
    }

    @SuppressLint("NewApi")
    private fun addHourToHourList(weather: Weather){
        _uiStateWeather.update { it.copy(hourList = mutableListOf()) }
        for(i in LocalTime.now().hour..< weather.forecast.forecastday[0].hour.size) {
            _uiStateWeather.value.hourList.add(weather.forecast.forecastday[0].hour[i])
        }
        for(i in 0..LocalTime.now().hour){
            _uiStateWeather.value.hourList.add(weather.forecast.forecastday[1].hour[i])
        }
    }

    private fun convertCoordinate(res: Coordinate): Triple<String,String, String>{
        val position =
            res.response.GeoObjectCollection.featureMember[0].GeoObject.Point.pos.split(" ")
                .reversed().joinToString(",")
        val cityName = res.response.GeoObjectCollection.featureMember[0].GeoObject.name
        var cityFullName = res.response.GeoObjectCollection.featureMember[0].GeoObject.metaDataProperty.GeocoderMetaData.text
        val cityNameItems = cityFullName.split(", ").toMutableList()
        if(cityNameItems.size > 1) {
            cityNameItems.removeFirst()
        }
        cityFullName = cityNameItems.joinToString(", ")
        return Triple(position, cityName, cityFullName)
    }


    fun changeDirectionToSearch(){
        _uiStateWeather.update { it.copy(currentEnterDirection = AnimatedContentTransitionScope.SlideDirection.Right,
            currentExitDirection = AnimatedContentTransitionScope.SlideDirection.Left)
        }
    }

    fun changeDirectionToSettings(){
        _uiStateWeather.update { it.copy(currentEnterDirection = AnimatedContentTransitionScope.SlideDirection.Left,
            currentExitDirection = AnimatedContentTransitionScope.SlideDirection.Right)
        }
    }

    companion object{
        private const val TIMEOUT_MILLIS = 5_000L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MainApp)
                val weatherRepository = application.appComponent.weatherRepository
                val coordinateRepository = application.appComponent.coordinateRepository
                val citiesRepository = application.databaseComponent.citiesRepository
                WeatherViewModel(weatherRepository = weatherRepository,
                    coordinateRepository = coordinateRepository,
                    citiesRepository = citiesRepository)
            }
        }
    }
}
