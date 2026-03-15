package com.john.kmpapplication.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    title: (@Composable () -> Unit)? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    topBarColors: TopAppBarColors? = null,
    floatingActionButton: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollConnection = scrollBehavior.nestedScrollConnection

    Scaffold(
        modifier = Modifier.nestedScroll(scrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AnimatedContent(
                targetState = title,
                transitionSpec = { fadeIn() togetherWith fadeOut() }
            ) { title ->
                title?.let {
                    TopAppBar(
                        title = { title.invoke() },
                        scrollBehavior = scrollBehavior,
                        colors = topBarColors ?: TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        navigationIcon = {
                            navigationIcon?.invoke()
                        }
                    )
                }
            }

        },
        floatingActionButton = floatingActionButton,
        bottomBar = {
            val isVisible = scrollBehavior.state.heightOffset > -0.5f
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                bottomBar()
            }

        },
    ) { innerPadding ->
        content(innerPadding)
    }
}