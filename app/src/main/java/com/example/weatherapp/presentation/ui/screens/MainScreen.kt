package com.example.weatherapp.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weatherapp.R
import com.example.weatherapp.data.api.model.Day
import com.example.weatherapp.data.api.model.Forecastday
import com.example.weatherapp.data.api.model.Hour
import com.example.weatherapp.data.api.model.Weather
import com.example.weatherapp.presentation.ui.WeatherViewModel
import com.example.weatherapp.utils.WeatherAppScreens
import com.example.weatherapp.utils.WeatherResult
import kotlin.math.roundToInt

@Composable
fun MainScreen(
    navController: NavHostController,
    state: WeatherResult,
    city: String,
    onClick: () -> Unit,
    weatherViewModel: WeatherViewModel,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(topBar = {
        MainTopBar(
            city = when(state){
                is WeatherResult.Success -> state.data.location.name
                is WeatherResult.Error, WeatherResult.Loading -> ""
            },
            onBackButtonClick = onBackButtonClick)
    }
    ){ innerPadding->
        Column{
            when (state) {
                is WeatherResult.Success -> SuccessScreen(
                    weatherViewModel = weatherViewModel,
                    weather = state.data,
                    modifier = Modifier.padding(top = innerPadding.calculateTopPadding(), bottom = innerPadding.calculateBottomPadding())
                )

                is WeatherResult.Error -> ErrorScreen()
                is WeatherResult.Loading -> LoadingScreen(
                    navController = navController,
                    weatherViewModel = weatherViewModel
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    city: String,
    onBackButtonClick: () -> Unit,
){
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Spacer(modifier = Modifier.width(24.dp))
                Text(text = city, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)

                IconButton(onClick =  onBackButtonClick) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@Composable
fun SuccessScreen(weather: Weather,
                  weatherViewModel: WeatherViewModel,
                  modifier: Modifier = Modifier
) {
    val painter = "https://${weather.current.condition.icon}"
    val degrees = "${weather.current.tempCelsius.roundToInt()}\u00B0"
    val context = LocalContext.current
    val condition = weather.current.condition.text

    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()){
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = degrees,
                    fontSize = 28.sp
                )
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(painter)
                        .crossfade(true)
                        .build(),
                    contentDescription = condition,
                    modifier = Modifier.size(64.dp)
                )
            }
            Text(text = condition)
        }
        Text(text = stringResource(R.string.three_day_forecast), modifier = Modifier.padding(horizontal = 8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(weather.forecast.forecastday){ forecastday ->
                ForecastCard( weatherViewModel = weatherViewModel, forecastday = forecastday,)
            }
        }
        Text(text = stringResource(R.string.today), modifier = Modifier.padding(horizontal = 8.dp))
        HourColumn(weatherViewModel.hourList, modifier = Modifier.padding(horizontal = 8.dp), weatherViewModel = weatherViewModel)
    }
}

@Composable
fun HourColumn(listHour: List<Hour>,
               weatherViewModel: WeatherViewModel,
               modifier: Modifier = Modifier){
    Column(modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)){
        for(i in listHour){
            HourCard(hour = i, weatherViewModel = weatherViewModel)
        }
    }
}
@Composable
fun HourCard(
    hour: Hour,
    weatherViewModel: WeatherViewModel){
    val context = LocalContext.current
    val painter = "https://${hour.condition.icon}"
    val condition = hour.condition.text
    val degrees = "${hour.temp_c.roundToInt()}\u00B0"

    Card{
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp)){
            Text(text = weatherViewModel.formatTime(hour.time), modifier = Modifier.weight(1f))
            AsyncImage(model = ImageRequest.Builder(context)
                .data(painter)
                .crossfade(true)
                .build(),
                contentDescription = condition,
                modifier = Modifier.size(64.dp))
            Text(text = degrees)
        }
    }
}
@Composable
fun ForecastCard(
    forecastday: Forecastday,
    weatherViewModel: WeatherViewModel,
    modifier: Modifier = Modifier) {
    val painter = "https://${forecastday.day.condition.icon}"
    val condition = forecastday.day.condition.text
    val context = LocalContext.current
    
    Card(modifier = modifier.width(175.dp)){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = weatherViewModel.formatDate(forecastday.date, week = false))
            Text(text = weatherViewModel.formatDate(forecastday.date, week = true))
            AsyncImage(model = ImageRequest.Builder(context)
                .data(painter)
                .crossfade(true)
                .build(),
                contentDescription = condition,
                modifier = Modifier.size(64.dp))
            Text(text = stringResource(R.string.day_temperature, forecastday.day.maxtemp_c.roundToInt()))
            Text(text = stringResource(R.string.night_temperature, forecastday.day.mintemp_c.roundToInt()))

        }
    }
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier
){
    Image(
        painter = painterResource(R.drawable.ic_connection_error),
        contentDescription = stringResource(R.string.error),
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun LoadingScreen(
    navController: NavHostController,
    weatherViewModel: WeatherViewModel,
    modifier: Modifier = Modifier
){
    if(weatherViewModel.isLoaded) {
        Image(
            painter = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading),
            modifier = modifier.fillMaxSize()
        )
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxSize()){
            Text(text = stringResource(R.string.loading_maintext))
            Text(text = stringResource(R.string.loading_choose_text),
                textDecoration = TextDecoration.Underline,
                color = Color.Blue,
                modifier = Modifier.clickable { navController.navigate(route = WeatherAppScreens.SearchScreen.name) })
        }
    }
}