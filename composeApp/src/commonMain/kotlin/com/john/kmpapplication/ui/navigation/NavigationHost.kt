package com.john.kmpapplication.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.john.kmpapplication.ui.component.rememberTopBarHostState
import com.john.kmpapplication.ui.product.*
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationHost() {
    val navController = rememberNavController()
    val snackbarHostState = SnackbarHostState()
    val topBarHostState = rememberTopBarHostState()
    val scrollConnection = topBarHostState.scrollBehavior?.nestedScrollConnection
        ?: remember { object : NestedScrollConnection {} }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            AnimatedContent(
                targetState = topBarHostState.content,
                transitionSpec = { fadeIn() togetherWith fadeOut() }
            ) { targetContent ->
                targetContent?.let { currentContent ->
                    TopAppBar(
                        scrollBehavior = topBarHostState.scrollBehavior,
                        title = { currentContent() },
                        colors = topBarHostState.topBarColors ?: TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            navController = navController,
            startDestination = ProductScreen,
        ) {
            composable<ProductScreen> {
                val viewModel = koinViewModel<ProductViewModel>()

                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                ProductScreen(
                    modifier = Modifier.fillMaxSize(),
                    uiState = uiState,
                    navController = navController,
                    uiEffect = viewModel.uiEffect,
                    snackbarHostState = snackbarHostState,
                    topBarHostState = topBarHostState,
                    onEvent = {
                        viewModel.onEvent(it)
                    }
                )
            }

            composable<ProductDetailScreen> { backStackEntry ->
                val productDetailScreen = backStackEntry.toRoute<ProductDetailScreen>()
                val viewModel = koinViewModel<ProductDetailViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                ProductDetailsScreen(
                    navController = navController,
                    id = productDetailScreen.productId ?: -1,
                    uiState = uiState,
                    uiEffect = viewModel.uiEffect,
                    snackbarHostState = snackbarHostState,
                    onEvent = { viewModel.onEvent(it) }
                )
            }
        }
    }

}

