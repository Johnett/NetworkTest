package com.jm.networktest

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager

class NetworkTestApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // provide custom configuration
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

        //initialize WorkManager
        WorkManager.initialize(this, config)
    }
}