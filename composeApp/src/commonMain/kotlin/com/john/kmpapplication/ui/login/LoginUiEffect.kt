package com.john.kmpapplication.ui.login

sealed interface LoginUiEffect {

    data class ShowSnackbar(
        val message: String, val actionLabel: String? = null
    ) : LoginUiEffect

    data object NavigateToSignUp : LoginUiEffect
    data object NavigateBack: LoginUiEffect
}