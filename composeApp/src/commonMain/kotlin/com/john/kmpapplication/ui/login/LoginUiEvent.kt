package com.john.kmpapplication.ui.login

import com.john.kmpapplication.ui.component.Screen

sealed interface LoginUiEvent {

    data class OnEmailChanged(val email: String) : LoginUiEvent
    data class OnPasswordChanged(val password: String) : LoginUiEvent
    data class OnLoginButtonClick(val email: String, val password: String) : LoginUiEvent
    data class Navigate(val screen: Screen) : LoginUiEvent
}