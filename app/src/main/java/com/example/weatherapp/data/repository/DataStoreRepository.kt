package com.example.weatherapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

// Key for storing dynamic theme preference
const val THEME_TYPE = "theme_type"

enum class ThemeType { SYSTEM, LIGHT, DARK }

class DataStoreRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val themeType = intPreferencesKey(THEME_TYPE)
    }

    val themeTypeFlow: Flow<ThemeType> = dataStore.data.map { preferences ->
        ThemeType.entries[preferences[themeType] ?: 0]
    }

    fun setThemeType(value: ThemeType, scope: CoroutineScope) {
        scope.launch {
            dataStore.edit { preferences ->
                preferences[themeType] = value.ordinal
            }
        }
    }
}