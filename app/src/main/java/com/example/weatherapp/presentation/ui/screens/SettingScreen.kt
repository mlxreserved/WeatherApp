package com.example.weatherapp.presentation.ui.screens

import android.widget.RadioButton
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
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
import com.example.weatherapp.R
import com.example.weatherapp.presentation.ui.models.SettingsModel
import com.example.weatherapp.presentation.ui.models.ThemeTypeState
import com.example.weatherapp.presentation.ui.models.WeatherViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
                  settingState: ThemeTypeState,
                  onBackButtonClick: () -> Unit,
                  settingsModel: SettingsModel,
                  modifier: Modifier = Modifier){

    val scope = rememberCoroutineScope()

   Scaffold(topBar = { SettingScreenTopBar(onBackButtonClick = onBackButtonClick)}) { innerPadding ->
       RadioButtons(model = settingsModel, settingState = settingState, modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
   }
}

@Composable
fun RadioButtons(model: SettingsModel, settingState: ThemeTypeState, modifier: Modifier = Modifier){
    Column(modifier = modifier){
        settingState.radioItems.forEach{
            Row(
                modifier = Modifier.selectable(
                    selected = (it.value == settingState.selectedRadio),
                    onClick = { model.updateThemeType(it.value) }
                )
            ) {
                RadioButton(
                    selected = (it.value == settingState.selectedRadio),
                    onClick = { model.updateThemeType(it.value) }
                )
                Text(text = it.title)
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