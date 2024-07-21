package com.example.weatherapp.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.R
import com.example.weatherapp.data.api.model.Weather
import com.example.weatherapp.presentation.ui.screens.MainScreen
import com.example.weatherapp.presentation.ui.screens.SearchScreen
import com.example.weatherapp.utils.WeatherAppScreens
import com.example.weatherapp.utils.WeatherResult
import javax.inject.Inject


@Composable
fun WeatherApp(navController: NavHostController,
               modifier: Modifier = Modifier){

    val weatherViewModel: WeatherViewModel = viewModel(factory = WeatherViewModel.Factory)
    val state = weatherViewModel.weatherUiState
    val city = weatherViewModel.textFieldCity
    val keyboardController = LocalFocusManager.current
    Surface{
        NavHost(
            navController = navController,
            startDestination = WeatherAppScreens.MainScreen.name
        ) {
            composable(route = WeatherAppScreens.MainScreen.name) {
                MainScreen(
                    state = state,
                    navController = navController,
                    city = city,
                    onClick = {
                        weatherViewModel.getWeather(city = city)
                        navController.navigate(WeatherAppScreens.SearchScreen.name)
                    },
                    weatherViewModel = weatherViewModel,
                    onBackButtonClick = {navController.navigate(route = WeatherAppScreens.SearchScreen.name)},
                )
            }
            composable(route = WeatherAppScreens.SearchScreen.name) {
                SearchScreen(
                    city = city,
                    onSearch = {
                        keyboardController.clearFocus()
                        weatherViewModel.getMultiCoordinate(city = city)
                    },
                    navController = navController,
                    weatherViewModel = weatherViewModel,
                    onBackButtonClick = {
                        keyboardController.clearFocus()
                        weatherViewModel.closeSearchScreen()
                        navController.navigateUp() }
                )
            }
        }
    }
}






