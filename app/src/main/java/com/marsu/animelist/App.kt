package com.marsu.animelist

import android.app.Application
import android.content.Context

class App : Application() {
    companion object {
        lateinit var appContext: Context
        var sfw : Boolean = true
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        val sharedPref = appContext.getSharedPreferences(
            "SETTINGS_PREF", Context.MODE_PRIVATE
        )
        sfw = (sharedPref.getBoolean("SFW", true))
    }
}