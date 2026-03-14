package com.john.kmpapplication.ui.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
class TopBarHostState {
    var content by mutableStateOf<(@Composable () -> Unit)?>(null)
    var scrollBehavior by mutableStateOf<TopAppBarScrollBehavior?>(null)
    var topBarColors by mutableStateOf<TopAppBarColors?>(null)

    fun update(
        behavior: TopAppBarScrollBehavior? = null,
        colors: TopAppBarColors? = null,
        newContent: (@Composable () -> Unit)? = null
    ) {
        content = newContent
        scrollBehavior = behavior
        topBarColors = colors
    }
    fun reset() {
        content = null
        scrollBehavior = null
        scrollBehavior?.state?.heightOffset = 0f
    }
}

@Composable
fun rememberTopBarHostState() = remember { TopBarHostState() }