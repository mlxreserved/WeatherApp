package com.example.weatherapp.presentation.ui.screens

import android.widget.EditText
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.presentation.ui.WeatherViewModel

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.R
import com.example.weatherapp.data.api.model.Weather
import com.example.weatherapp.utils.WeatherAppScreens
import com.example.weatherapp.utils.WeatherResult






@Composable
fun WeatherApp(navController: NavHostController,
               modifier: Modifier = Modifier){

    val weatherViewModel: WeatherViewModel = viewModel()
    val state = weatherViewModel.weatherUiState
    val city = weatherViewModel.textFieldCity
    val lang = weatherViewModel.lang
    val keyboardController = LocalFocusManager.current
    val context = LocalContext.current


    Scaffold(
        topBar = {
            WeatherAppTopBar(
                city = city,
                weatherViewModel = weatherViewModel,
                onBackButtonClick = { },
                onSearch = {
                    weatherViewModel.getWeather(city = city, language = lang)
                    navController.navigate(WeatherAppScreens.RequestScreen.name)
                    keyboardController.clearFocus()},
                )
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = WeatherAppScreens.MainScreen.name
        ) {
            composable(route = WeatherAppScreens.MainScreen.name) {
                MainScreen(

                    city = city,
                    lang = lang,
                    onClick = {
                        weatherViewModel.getWeather(city = city, language = lang)
                        navController.navigate(WeatherAppScreens.RequestScreen.name)
                    },
                    weatherViewModel = weatherViewModel,
                    modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                )
            }
            composable(route = WeatherAppScreens.RequestScreen.name) {
                when (state) {
                    is WeatherResult.Success -> SuccessScreen(
                        weather = state.data,
                        innerPadding = innerPadding
                    )

                    is WeatherResult.Error -> ErrorScreen(innerPadding = innerPadding)
                    is WeatherResult.Loading -> LoadingScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppTopBar(
    onSearch: (KeyboardActionScope) -> Unit,
    city: String,
    weatherViewModel: WeatherViewModel,
    onBackButtonClick: () -> Unit) {
    TopAppBar(title = {
        Column {
            TextField(
                placeholder = { Text(text = stringResource(R.string.search)) },
                value = city,
                onValueChange = { weatherViewModel.updateCity(it) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                keyboardActions = KeyboardActions(
                    onSearch = onSearch
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                suffix = {
                    if (city.isNotBlank())
                        IconButton(onClick = {weatherViewModel.clearCity()}) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = stringResource(R.string.clear)
                            )
                        }
                    else
                        null
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
                      },
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        }
    )

}

@Composable
fun MainScreen(city: String,
               lang: String,
               onClick: () -> Unit,
               weatherViewModel: WeatherViewModel,
               modifier: Modifier = Modifier){
}


@Composable
fun SuccessScreen(weather: Weather,
                  modifier: Modifier = Modifier,
                  innerPadding: PaddingValues) {
    Column {
        Text(
            text = weather.current.tempCelsius.toString(),
            modifier = modifier.padding(top = innerPadding.calculateTopPadding())
        )
        Text(
            text = weather.location.name
        )
    }
}


@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues
){
    Image(
        painter = painterResource(R.drawable.ic_connection_error),
        contentDescription = stringResource(R.string.error))
}

@Composable
fun LoadingScreen(){



}

@Preview(showSystemUi = true)
@Composable
fun TopAppBarPreview(){
    WeatherAppTopBar(
        city = "",
        onBackButtonClick = {},
        weatherViewModel = WeatherViewModel(),
        onSearch = {},
    )
}


@Preview
@Composable
fun ErrorScreenPreview(){
    ErrorScreen(innerPadding = PaddingValues(8.dp))
}

