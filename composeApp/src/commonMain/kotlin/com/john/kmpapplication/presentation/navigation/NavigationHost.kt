package com.john.kmpapplication.presentation.navigation

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
import com.john.kmpapplication.presentation.ui.ProductScreen
import com.john.kmpapplication.presentation.viewmodel.ProductViewModel
import kotlinx.serialization.Serializable
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
        }
    }

}

@Serializable
data object ProductScreen