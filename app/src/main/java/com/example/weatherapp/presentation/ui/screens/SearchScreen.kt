package com.example.weatherapp.presentation.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.weatherapp.R
import com.example.weatherapp.presentation.ui.models.SearchUiState
import com.example.weatherapp.presentation.ui.models.WeatherAppUiState
import com.example.weatherapp.presentation.ui.models.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    city: String,
    onSearch: (KeyboardActionScope) -> Unit,
    weatherViewModel: WeatherViewModel,
    onBackButtonClick: () -> Unit
){
    TopAppBar(
        title = {
        Row(modifier = Modifier.fillMaxWidth()) {
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
                        IconButton(onClick = { weatherViewModel.clearCity() }) {
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        }
    )
}

@Composable
fun SearchScreen(
    stateSearch: SearchUiState,
    navController: NavHostController,
    state: WeatherAppUiState,
    onSearch: (KeyboardActionScope) -> Unit,
    weatherViewModel: WeatherViewModel,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier) {


    Scaffold (
        topBar = {
            SearchTopBar(
                city = state.textFieldCity,
                onSearch = onSearch,
                weatherViewModel = weatherViewModel,
                onBackButtonClick = onBackButtonClick)
        }
    ){ innerPadding ->
            if (state.textFieldCity.isNotBlank()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding())
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.coordinateList) { currentCity ->
                        SearchCard(city = currentCity, onClick = {

                            weatherViewModel.getWeather(currentCity, false)
                            weatherViewModel.closeSearchScreen()
                            navController.navigateUp()

                        })
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding() + 8.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(text = stringResource(R.string.history), fontSize = 24.sp)
                    }
                    items(stateSearch.storyOfSearch) { currentCity ->
                        SearchCard(city = currentCity.name, onClick = {
                            weatherViewModel.getWeather(currentCity.name, false)
                            weatherViewModel.closeSearchScreen()
                            navController.navigateUp()

                        })
                    }
                }
            }
    }
}


@Composable
fun SearchCard(
    city: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() } ,
        shape = RoundedCornerShape(0.dp)
    ){
        Row{
            Text(text = city, modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp))
        }
    }
}