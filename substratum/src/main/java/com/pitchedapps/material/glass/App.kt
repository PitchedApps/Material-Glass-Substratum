package com.pitchedapps.material.glass

import android.app.Application

/**
 * Created by Allan Wang on 2017-07-18.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Prefs.initialize(this, "${BuildConfig.APPLICATION_ID}.prefs")
    }

}