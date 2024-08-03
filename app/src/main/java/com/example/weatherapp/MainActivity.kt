package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.presentation.ui.WeatherApp
import com.example.weatherapp.presentation.ui.WeatherViewModel
import com.example.weatherapp.presentation.ui.theme.WeatherAppTheme
import com.example.weatherapp.utils.WeatherResult
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                val weatherViewModel: WeatherViewModel = viewModel(factory = WeatherViewModel.Factory)
//
//                val coroutineScope = rememberCoroutineScope()
//
//                coroutineScope.launch { weatherViewModel.getWeather("Москва") }

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
