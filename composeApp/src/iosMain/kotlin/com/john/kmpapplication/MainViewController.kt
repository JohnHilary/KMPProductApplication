package com.john.kmpapplication

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.john.kmpapplication.ui.App

fun MainViewController() = ComposeUIViewController {
    val imagePicker = ImagePicker()
    CompositionLocalProvider(LocalImagePicker provides imagePicker) {
        App()
    }
}