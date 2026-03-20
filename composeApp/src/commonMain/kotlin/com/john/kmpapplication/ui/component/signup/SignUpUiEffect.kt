package com.john.kmpapplication.ui.component.signup

sealed interface SignUpUiEffect {

    data class ShowSnackbar(
        val message: String, val actionLabel: String? = null
    ) : SignUpUiEffect

    data object NavigateToLogin: SignUpUiEffect
    data object NavigateBack: SignUpUiEffect
    data object NavigateToProfile: SignUpUiEffect

}