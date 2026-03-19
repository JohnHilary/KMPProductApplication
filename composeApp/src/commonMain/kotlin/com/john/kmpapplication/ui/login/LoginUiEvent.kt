package com.john.kmpapplication.ui.login

sealed interface LoginUiEvent {
    data class OnEmailChanged(val email: String) : LoginUiEvent
    data class OnPasswordChanged(val password: String) : LoginUiEvent
    data class OnLoginButtonClick(val email: String, val password: String) : LoginUiEvent
    data object OnSignUpButtonClick : LoginUiEvent
}