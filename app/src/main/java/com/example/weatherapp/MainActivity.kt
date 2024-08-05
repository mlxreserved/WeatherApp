package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.presentation.ui.WeatherApp
import com.example.weatherapp.presentation.ui.models.WeatherViewModel
import com.example.weatherapp.presentation.ui.theme.WeatherAppTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
//            val context = LocalContext.current
//
//            val dataStore = StoreTheme(context)
//
//            val savedTheme = dataStore.getTheme.collectAsState(initial = "")
//
            val weatherViewModel: WeatherViewModel = viewModel(factory = WeatherViewModel.Factory)
//
//            weatherViewModel.changeTheme(savedTheme.value!!.toBooleanStrictOrNull())
//
//            val settingState by weatherViewModel.settingState.collectAsState()
            WeatherAppTheme(/*darkTheme = settingState.currentTheme ?: isSystemInDarkTheme()*/){
                val navController = rememberNavController()
                Surface {
                    WeatherApp(
                        weatherViewModel = weatherViewModel,
                        navController = navController,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
