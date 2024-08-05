package com.example.weatherapp.presentation.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.presentation.ui.models.ModelProvider
import com.example.weatherapp.presentation.ui.models.SettingsModel
import com.example.weatherapp.presentation.ui.models.WeatherViewModel
import com.example.weatherapp.presentation.ui.screens.ForecastScreen
import com.example.weatherapp.presentation.ui.screens.MainScreen
import com.example.weatherapp.presentation.ui.screens.SearchScreen
import com.example.weatherapp.presentation.ui.screens.SettingScreen
import com.example.weatherapp.utils.WeatherAppScreens


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherApp(
    weatherViewModel: WeatherViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier){

    val settingsModel: SettingsModel = viewModel(factory = ModelProvider.Factory)
    val state by weatherViewModel.uiStateWeather.collectAsState()
    val stateSearch by weatherViewModel.uiStateSearch.collectAsState()
    val settingState by settingsModel.themeType.collectAsState()
    val city = state.textFieldCity


    val keyboardController = LocalFocusManager.current
    Surface{
        NavHost(
            navController = navController,
            startDestination = WeatherAppScreens.MainScreen.name
        ) {
                composable(route = WeatherAppScreens.MainScreen.name,
                    enterTransition = {
                        slideIntoContainer(
                            towards = state.currentEnterDirection,
                            animationSpec = tween(
                                durationMillis = 250,
                                easing = LinearEasing
                            )
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            towards = state.currentExitDirection,
                            animationSpec = tween(
                                durationMillis = 250,
                                easing = LinearEasing
                            )
                        )
                    }) {
                    MainScreen(
                        uiState = state,
                        weatherState = state.weatherUiState,
                        navController = navController,
                        onClick = {
                            weatherViewModel.changeDirectionToSearch()
                            weatherViewModel.changeSelectedItem(it)
                            navController.navigate(route = WeatherAppScreens.ForecastScreen.name)
                        },
                        weatherViewModel = weatherViewModel,
                        onSearchButtonClick = {
                            weatherViewModel.changeDirectionToSearch()
                            navController.navigate(route = WeatherAppScreens.SearchScreen.name)
                        },
                        onSettingsButtonClick = {
                            weatherViewModel.changeDirectionToSettings()
                            navController.navigate(route = WeatherAppScreens.SettingScreen.name)
                        },
                    )

                }
                composable(route = WeatherAppScreens.SearchScreen.name,
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(
                                durationMillis = 250,
                                easing = LinearEasing
                            )
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(
                                durationMillis = 250,
                                easing = LinearEasing
                            )
                        )
                    }) {
                    SearchScreen(
                        state = state,
                        stateSearch = stateSearch,
                        onSearch = {
                            keyboardController.clearFocus()
                            weatherViewModel.getMultiCoordinate(city = city)
                        },
                        navController = navController,
                        weatherViewModel = weatherViewModel,
                        onBackButtonClick = {
                            keyboardController.clearFocus()
                            navController.navigateUp()
                        }
                    )
                }
            composable(route = WeatherAppScreens.ForecastScreen.name,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = LinearEasing
                        )
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = LinearEasing
                        )
                    )
                }){
                ForecastScreen(item = state.selectedItem,uiState = state, weatherState = state.weatherUiState, weatherViewModel = weatherViewModel, onBackButtonClick = {navController.navigateUp()})
            }
            composable(route = WeatherAppScreens.SettingScreen.name,
                enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = LinearEasing
                    )
                )
            },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = LinearEasing
                        )
                    )
                }){
                SettingScreen(onBackButtonClick = { navController.navigateUp() }, settingState = settingState, settingsModel = settingsModel)
            }
        }
    }
}









