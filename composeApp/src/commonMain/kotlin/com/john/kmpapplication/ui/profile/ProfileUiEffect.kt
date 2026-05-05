package com.john.kmpapplication.ui.profile

import com.john.kmpapplication.ui.component.dialog.DialogRequest


sealed interface ProfileUiEffect {
    data class ShowSnackbar(
        val message: String, val actionLabel: String? = null
    ) : ProfileUiEffect
    data class Navigate(val screen : Any) : ProfileUiEffect
    data class ShowDialog(val dialogRequest: DialogRequest<ProfileUiEvent>) : ProfileUiEffect
}