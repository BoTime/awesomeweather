package com.botime.awesomeweather

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.io.File

@HiltAndroidApp
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()

        Timber.plant(object: Timber.DebugTree() {
            override fun log(
                priority: Int, tag: String?, message: String, t: Throwable?
            ) {
                super.log(priority, "Weather-$tag", message, t)
            }
        })
    }
}