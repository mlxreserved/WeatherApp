package com.example.weatherapp.di

import android.app.Application
import android.content.Context
import android.provider.ContactsContract.Data
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherapp.datastore.StoreTheme


class MainApp: Application() {

    lateinit var appComponent: AppComponent
    lateinit var databaseComponent: DatabaseComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
        databaseComponent = initDagger(this)
    }

    private fun initDagger(context: MainApp): DatabaseComponent = DaggerDatabaseComponent.builder().databaseModule(DatabaseModule(context)).build()
}