package com.john.kmpapplication

import android.app.Application
import com.john.kmpapplication.di.initKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}