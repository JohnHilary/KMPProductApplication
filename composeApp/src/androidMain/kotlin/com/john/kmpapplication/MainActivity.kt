package com.john.kmpapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.john.kmpapplication.ui.App
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    val imagePicker: ImagePicker by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        loadKoinModules(module {
            single<ComponentActivity> { this@MainActivity }
        })
        imagePicker
        setContent {
            CompositionLocalProvider(LocalImagePicker provides imagePicker) {
                App()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}