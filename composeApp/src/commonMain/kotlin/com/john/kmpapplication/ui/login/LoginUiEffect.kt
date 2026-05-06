package com.john.kmpapplication.ui.login

import com.john.kmpapplication.ui.component.Screen

sealed interface LoginUiEffect {

    data class ShowSnackbar(
        val message: String, val actionLabel: String? = null
    ) : LoginUiEffect

    data class Navigate(val screen : Screen) : LoginUiEffect
    data object NavigateBack: LoginUiEffect
}