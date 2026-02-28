package com.john.kmpapplication.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.john.kmpapplication.ui.navigation.NavigationHost

@Composable
@Preview
fun App() {
    MaterialTheme {
        NavigationHost()
    }
}