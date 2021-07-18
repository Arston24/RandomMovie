package com.victorsysuev.randommovie

import android.app.Application
import android.content.Context
import com.victorsysuev.randommovie.database.PreferenceRepository
import com.victorsysuev.randommovie.di.AppComponent
import com.victorsysuev.randommovie.di.DaggerAppComponent
import com.victorsysuev.randommovie.di.RoomModule

class App : Application() {

    lateinit var preferenceRepository: PreferenceRepository
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().roomModule(RoomModule(this)).build()
        preferenceRepository = PreferenceRepository(
            getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE)
        )
    }

    companion object {
        const val DEFAULT_PREFERENCES = "default_preferences"
    }
}
