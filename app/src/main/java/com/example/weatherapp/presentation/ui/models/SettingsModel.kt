package com.example.weatherapp.presentation.ui.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.repository.DataStoreRepository
import com.example.weatherapp.data.repository.ThemeType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SettingsModel(private val storeRepository: DataStoreRepository) : ViewModel() {

    val themeType: StateFlow<ThemeTypeState> =
        storeRepository.themeTypeFlow.map { ThemeTypeState(it) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemeTypeState(ThemeType.SYSTEM)
        )

    fun updateThemeType(themeType: ThemeType) {
        storeRepository.setThemeType(themeType, viewModelScope)
    }
}

//Holds selected theme and theme options.
data class ThemeTypeState(
    val selectedRadio: ThemeType,
    val radioItems: List<RadioItem> = listOf(
        RadioItem(ThemeType.SYSTEM, "System"),
        RadioItem(ThemeType.LIGHT, "Light"),
        RadioItem(ThemeType.DARK, "Dark"),
    ),
)

//Simple holder for theme type and its title (for radio buttons).
data class RadioItem(val value: ThemeType, val title: String)