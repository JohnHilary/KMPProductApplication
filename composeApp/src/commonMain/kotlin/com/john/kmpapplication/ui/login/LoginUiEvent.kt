package com.john.kmpapplication.ui.login

sealed interface LoginUiEvent {

    data class OnUsernameChanged(val username: String) : LoginUiEvent
    data class OnPasswordChanged(val password: String) : LoginUiEvent
    data class OnLoginButtonClick(val username: String, val password: String) : LoginUiEvent
    data object OnSignUpButtonClick : LoginUiEvent
}