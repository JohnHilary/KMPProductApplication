package com.john.kmpapplication.di

import com.john.kmpapplication.ui.product.appModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}