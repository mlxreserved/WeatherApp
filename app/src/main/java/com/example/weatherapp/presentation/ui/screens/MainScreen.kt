package com.example.weatherapp.presentation.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weatherapp.R
import com.example.weatherapp.data.api.model.Forecastday
import com.example.weatherapp.data.api.model.Hour
import com.example.weatherapp.presentation.ui.models.WeatherAppUiState
import com.example.weatherapp.presentation.ui.models.WeatherViewModel
import com.example.weatherapp.utils.WeatherResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    uiState: WeatherAppUiState,
    navController: NavHostController,
    weatherState: WeatherResult,
    onClick: (Int) -> Unit,
    weatherViewModel: WeatherViewModel,
    onSearchButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val city = when(weatherState){
        is WeatherResult.Success -> weatherState.data.location.name
        is WeatherResult.Error, WeatherResult.Loading -> ""
    }
    val refreshing = when(weatherState){
        is WeatherResult.Loading -> true //uiState.isLoaded
        is WeatherResult.Error -> false
        is WeatherResult.Success -> false
    }


    val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = {   weatherViewModel.getWeather(city, true)})


    Scaffold(topBar = {
         MainTopBar(
             city,
             onSearchButtonClick= onSearchButtonClick,
             onSettingsButtonClick = onSettingsButtonClick)
    }
    ) { innerPadding ->
        Box(
            Modifier.pullRefresh(pullRefreshState)
        ) {


                when (weatherState) {

                    is WeatherResult.Success ->

                        SuccessScreen(
                            weatherViewModel = weatherViewModel,
                            onClick = onClick,
                            forecastday = weatherState.data.forecast.forecastday,
                            painter = "https://${weatherState.data.current.condition.icon}",
                            condition = weatherState.data.current.condition.text,
                            degrees = "${weatherState.data.current.tempCelsius.roundToInt()}\u00B0",
                            uiState = uiState,
                            modifier = Modifier.padding(
                                top = innerPadding.calculateTopPadding(),
                                bottom = innerPadding.calculateBottomPadding()
                            )
                        )

                    is WeatherResult.Error -> ErrorScreen()

                    is WeatherResult.Loading ->
                        LoadingScreen(
                            uiState = uiState,
                            navController = navController,
                        )
                }
                PullRefreshIndicator(
                    refreshing,
                    pullRefreshState,
                    Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = innerPadding.calculateTopPadding())
                )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    city: String,
    onSettingsButtonClick: () -> Unit,
    onSearchButtonClick: () -> Unit
){
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(text = city, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)

                IconButton(onClick =  onSearchButtonClick) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onSettingsButtonClick ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(R.string.settings)
                )
            }
        }
    )
}



@Composable
fun SuccessScreen(uiState: WeatherAppUiState,
                  onClick: (Int) -> Unit,
                  forecastday: List<Forecastday>,
                  painter: String,
                  condition: String,
                  degrees: String,
                  weatherViewModel: WeatherViewModel,
                  modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LazyColumn(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)){
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                items(forecastday.size){ item ->
                    ForecastCard( forecastday = forecastday[item],
                        painter = "https://${forecastday[item].day.condition.icon}",
                        condition = forecastday[item].day.condition.text,
                        item = item,
                        onClick = onClick,
                        weatherViewModel = weatherViewModel )
                }
            }
            Text(text = stringResource(R.string.today), modifier = Modifier.padding(horizontal = 8.dp))
        }

        /*Row(modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            repeat(forecastday.size){ forecastdayIndex ->
                ForecastCard(
                    forecastday = forecastday[forecastdayIndex],
                    painter = "https://${forecastday[forecastdayIndex].day.condition.icon}",
                    condition = forecastday[forecastdayIndex].day.condition.text,
                    weatherViewModel = weatherViewModel)

            }
        }*/

        items(items = uiState.hourList,
            key = {
                it.time_epoch
            }
        ) { hour ->
            HourCard(hour = hour,
                painter = "https://${hour.condition.icon}",
                condition = hour.condition.text,
                degrees = "${hour.temp_c.roundToInt()}\u00B0",
                weatherViewModel = weatherViewModel )
        }

        //HourColumn(uiState.hourList, modifier = Modifier.padding(horizontal = 8.dp), weatherViewModel = weatherViewModel)
    }
}

@Composable
fun HourColumn(listHour: List<Hour>,
               weatherViewModel: WeatherViewModel,
               modifier: Modifier = Modifier){
    Column(modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)){
        for(i in listHour){
            HourCard(hour = i,
                painter = "https://${i.condition.icon}",
                condition = i.condition.text,
                degrees = "${i.temp_c.roundToInt()}\u00B0",
                weatherViewModel = weatherViewModel )
        }
    }
}
@Composable
fun HourCard(
    hour: Hour,
    painter: String,
    condition: String,
    degrees: String,
    weatherViewModel: WeatherViewModel,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current

    Card{
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp).height(64.dp)){
            Text(
                text = if(hour.time.substring(hour.time.length-5) != "00:00")
                    weatherViewModel.formatTime(hour.time, false)
                else
                    weatherViewModel.formatTime(hour.time, true),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp))
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
    item: Int,
    painter: String,
    condition: String,
    onClick: (Int) -> Unit,
    weatherViewModel: WeatherViewModel,
    modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val interactionSource = remember{ MutableInteractionSource() }
    Card(
        modifier = modifier
            .width(175.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick(item) }){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = weatherViewModel.formatDate(forecastday.date, week = false, short = false))
            Text(text = weatherViewModel.formatDate(forecastday.date, week = true, short = false))
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
    uiState: WeatherAppUiState,
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    /*if(!uiState.isLoaded) {
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
    else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){

        }
        *//*Image(
            painter = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading),
            modifier = modifier.fillMaxSize()
        )*//*
    }*/
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){}
}