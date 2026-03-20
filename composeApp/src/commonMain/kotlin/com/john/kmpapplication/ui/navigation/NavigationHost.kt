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
import com.john.kmpapplication.ui.component.signup.SignUpScreen
import com.john.kmpapplication.ui.component.signup.SignUpViewModel
import com.john.kmpapplication.ui.login.LoginScreen
import com.john.kmpapplication.ui.login.LoginViewModel
import com.john.kmpapplication.ui.product.*
import com.john.kmpapplication.ui.profile.MyProfile
import com.john.kmpapplication.ui.profile.MyProfileScreen
import com.john.kmpapplication.ui.profile.ProfileViewModel
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

        composable<ProductDetailScreen> { _ ->
            val viewModel = koinViewModel<ProductDetailViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ProductDetailsScreen(
                navController = navController,
                uiState = uiState,
                uiEffect = viewModel.uiEffect,
                onEvent = { viewModel.onEvent(it) }
            )
        }

        composable<MyProfile> {
            val viewModel = koinViewModel<ProfileViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            MyProfileScreen(
                navController = navController,
                uiState = uiState,
                uiEffect = viewModel.uiEffect,
                dialogState = viewModel.dialogState,
                onEvent = { viewModel.onEvent(it) }
            )
        }

        composable<LoginScreen> {
            val viewModel = koinViewModel<LoginViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            LoginScreen(
                navController = navController,
                uiState = uiState,
                uiEffect = viewModel.uiEffect,
            ) {
                viewModel.onEvent(it)
            }
        }
        composable<SignUpScreen> {
            val viewModel = koinViewModel<SignUpViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            SignUpScreen(
                navController = navController,
                uiState = uiState,
                uiEffect = viewModel.uiEffect,
            ) {
                viewModel.onEvent(it)
            }

        }
    }
}


