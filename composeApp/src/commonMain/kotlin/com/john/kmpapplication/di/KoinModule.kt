package com.john.kmpapplication.di

import com.john.kmpapplication.platformModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin() {
    initKoinAndroid(appDeclaration = {})
}

fun initKoinAndroid(
    appDeclaration: KoinAppDeclaration = {}
) {
    startKoin {
        appDeclaration()
        modules(appModule + platformModule())
    }
}

