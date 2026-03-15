package com.john.kmpapplication.ui.profile


sealed interface ProfileUiEffect {
    data class ShowSnackbar(
        val message: String, val actionLabel: String? = null
    ) : ProfileUiEffect

}