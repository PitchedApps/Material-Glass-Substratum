package com.pitchedapps.material.glass

import android.app.Application
import java.util.*

/**
 * Created by Allan Wang on 2017-07-18.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Prefs.initialize(this, "${BuildConfig.APPLICATION_ID}.prefs")
        if (Prefs.installDate == -1L) Prefs.installDate = System.currentTimeMillis()
        if (Prefs.identifier == -1) Prefs.identifier = Random().nextInt(Int.MAX_VALUE)
    }

}