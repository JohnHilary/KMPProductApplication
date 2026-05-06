package com.john.kmpapplication.ui.profile



sealed interface ProfileUiEffect {
    data class ShowSnackbar(
        val message: String, val actionLabel: String? = null, val onAction: ProfileUiEvent? = null
    ) : ProfileUiEffect
    data class Navigate(val screen : Any) : ProfileUiEffect
}