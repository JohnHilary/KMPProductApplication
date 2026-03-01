package com.john.kmpapplication.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.john.kmpapplication.ui.product.*
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NavigationHost() {
    val navController = rememberNavController()
    val snackbarHostState = SnackbarHostState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.fillMaxSize().padding(bottom = innerPadding.calculateBottomPadding()),
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

