package com.john.kmpapplication.ui.userform

sealed interface UserFormUiEffect {

    data class ShowSnackbar(
        val message: String, val actionLabel: String? = null
    ) : UserFormUiEffect

    data object NavigateToLogin: UserFormUiEffect
    data object NavigateBack: UserFormUiEffect
    data object NavigateToProfile: UserFormUiEffect

}