package com.example.weatherapp.presentation.ui.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.repository.DataStoreRepository
import com.example.weatherapp.data.repository.ThemeType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ThemeModel(storeRepository: DataStoreRepository) : ViewModel() {
    // Observe the DataStore flow for theme type preference
    val isDarkTheme: StateFlow<ThemeType> =
        storeRepository.themeTypeFlow.map { it }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemeType.SYSTEM
        )
}