package com.john.kmpapplication

import android.app.Application
import com.john.kmpapplication.di.initKoinAndroid
import org.koin.android.ext.koin.androidContext

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoinAndroid {
            androidContext(this@MyApp)
        }
    }
}