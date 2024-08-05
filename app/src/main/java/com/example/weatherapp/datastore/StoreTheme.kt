package com.example.weatherapp.datastore

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//class StoreTheme(val context: Context) {
//
//    companion object{
//        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Theme")
//        val THEME_KEY = stringPreferencesKey("theme")
//    }
//
//    val getTheme: Flow<String?> = context.dataStore.data
//        .map { preferences ->
//            preferences[THEME_KEY] ?: ""
//        }
//
//    suspend fun saveTheme(theme: String){
//        context.dataStore.edit { preferences ->
//            preferences[THEME_KEY] = theme
//        }
//    }
//}