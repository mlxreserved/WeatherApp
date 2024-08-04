package com.example.weatherapp.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.datastore.dataStore
import com.example.weatherapp.R
import com.example.weatherapp.datastore.StoreTheme
import com.example.weatherapp.presentation.ui.SettingState
import com.example.weatherapp.presentation.ui.WeatherViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(storeTheme: StoreTheme,
                  settingState: SettingState,
                  onBackButtonClick: () -> Unit,
                  weatherViewModel: WeatherViewModel,
                  modifier: Modifier = Modifier){

    val scope = rememberCoroutineScope()

   Scaffold(topBar = { SettingScreenTopBar(onBackButtonClick = onBackButtonClick)}) { innerPadding ->
       Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())){
           Row{
               RadioButton(selected = settingState.currentTheme == false,
                   onClick = {
                       scope.launch{
                           storeTheme.saveTheme(false.toString())
                           weatherViewModel.changeTheme(false)
                       }
                   }
               )
               Text(text = stringResource(R.string.light_theme))
           }
           Row{
               RadioButton(selected = settingState.currentTheme == true,
                   onClick = {
                       scope.launch {
                           storeTheme.saveTheme(true.toString())
                           weatherViewModel.changeTheme(true)
                       }
                   }
               )
               Text(text = stringResource(R.string.dark_theme))
           }
           Row{
               RadioButton(selected = settingState.currentTheme == null,
                   onClick = {
                       scope.launch {
                           storeTheme.saveTheme(null.toString())
                           weatherViewModel.changeTheme(null)
                       }
                   }
               )
               Text(text = stringResource(R.string.auto_theme))
           }
       }
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreenTopBar(onBackButtonClick: () -> Unit,
                        modifier: Modifier = Modifier){
    TopAppBar(title = {
        Text(text = stringResource(R.string.settings))
    },
        navigationIcon = {
            IconButton(onClick = onBackButtonClick ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back))
            }
        })
}