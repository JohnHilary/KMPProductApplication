package com.john.kmpapplication

import android.app.Application
import com.john.kmpapplication.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(listOf(platformModule)) {
            androidContext(this@MyApp)
        }
    }
}