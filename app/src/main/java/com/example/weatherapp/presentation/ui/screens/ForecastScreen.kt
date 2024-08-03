package com.example.weatherapp.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weatherapp.R
import com.example.weatherapp.data.api.model.Forecastday
import com.example.weatherapp.presentation.ui.WeatherAppUiState
import com.example.weatherapp.presentation.ui.WeatherViewModel
import com.example.weatherapp.utils.WeatherResult
import kotlin.math.roundToInt

@Composable
fun ForecastScreen(item: Int,
                   uiState: WeatherAppUiState,
                   weatherState: WeatherResult,
                   onBackButtonClick: () -> Unit,
                   weatherViewModel: WeatherViewModel,
                   modifier: Modifier = Modifier){
    val forecastday = when(weatherState) {
        is WeatherResult.Success -> weatherState.data.forecast.forecastday
        else -> emptyList()
    }
    Scaffold (
        topBar = { ForecastdayTopBar(onBackButtonClick)}
    ){ innerPadding ->
        Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()).fillMaxWidth()) {
            LazyRow (horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()){
                itemsIndexed(forecastday) { index, forecastday ->
                    val dayOfWeek =
                        weatherViewModel.formatDate(forecastday.date, week = true, short = true)
                    val dayOfMonth =
                        weatherViewModel.formatDate(forecastday.date, week = false, short = true)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable{weatherViewModel.changeSelectedItem(index)}.padding(8.dp)) {
                        Text(text = dayOfWeek, fontSize = 24.sp, modifier = Modifier.padding(vertical = 8.dp))
                        Box(contentAlignment = Alignment.Center){
                            if(index == uiState.selectedItem){
                                Circle(color = Color.Gray)
                            }
                            Text(text = dayOfMonth, fontSize = 24.sp)
                        }
                    }
                }
            }
            TemperatureCard(
                forecastday = forecastday,
                forecastdayNum = item
            )
        }
    }
}

@Composable
fun Circle(color: Color,
           modifier: Modifier = Modifier){
    Box(
        modifier = modifier.size(40.dp)
                .clip(CircleShape)
                .background(color)

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastdayTopBar(onBackButtonClick: () -> Unit,
                      modifier: Modifier = Modifier){
    TopAppBar(title = { Text(text = stringResource(R.string.three_day_forecast)) },
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        })
}

@Composable
fun TemperatureCard(
    forecastday: List<Forecastday>,
    forecastdayNum: Int,
    modifier: Modifier = Modifier) {

    val context = LocalContext.current

    Card(modifier = modifier
        .padding(horizontal = 8.dp)
        .fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()){
            repeat(4) {
                val index = (24/4-1) + (it*6)
                val painter = "https://${forecastday[forecastdayNum].hour[index].condition.icon}"
                val temperature = forecastday[forecastdayNum].hour[index].temp_c.roundToInt().toString() + "°"
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Text(text = when(it){
                        0 -> stringResource(R.string.morning)
                        1 -> stringResource(R.string.day)
                        2 -> stringResource(R.string.evening)
                        else -> stringResource(R.string.night)
                    }, fontSize = 24.sp)
                    AsyncImage(model = ImageRequest.Builder(context)
                        .data(painter)
                        .crossfade(true)
                        .build(),
                        contentDescription = forecastday[forecastdayNum].hour[index].condition.text,
                        modifier = Modifier.size(48.dp))
                    Text(text = temperature, fontSize = 24.sp)
                }
            }
        }
    }
}