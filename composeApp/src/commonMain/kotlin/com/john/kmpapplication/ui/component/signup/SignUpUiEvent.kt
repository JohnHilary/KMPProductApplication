package com.john.kmpapplication.ui.component.signup



sealed interface SignUpUiEvent {
    data class OnUsernameChanged(val username: String) : SignUpUiEvent
    data class OnEmailChanged(val email: String) : SignUpUiEvent
    data class OnPasswordChanged(val password: String) : SignUpUiEvent
    data object OnSignUpButtonClick : SignUpUiEvent
    data object OnLoginButtonClick : SignUpUiEvent
}