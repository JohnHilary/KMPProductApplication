package com.john.kmpapplication

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.john.kmpapplication.di.initKoin

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KMPApplication",
    ) {
        initKoin()
        App()
    }
}