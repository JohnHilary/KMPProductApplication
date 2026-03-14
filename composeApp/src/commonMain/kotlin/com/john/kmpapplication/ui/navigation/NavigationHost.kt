package com.john.kmpapplication.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    NavHost(
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
                onEvent = {
                    viewModel.onEvent(it)
                }
            )
        }

        composable<ProductDetailScreen> { backStackEntry ->
            val viewModel = koinViewModel<ProductDetailViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ProductDetailsScreen(
                navController = navController,
                uiState = uiState,
                uiEffect = viewModel.uiEffect,
                onEvent = { viewModel.onEvent(it) }
            )
        }
    }
}


