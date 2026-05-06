package com.john.kmpapplication.ui.profile

import com.john.kmpapplication.ui.component.Screen
import com.john.kmpapplication.ui.component.dialog.DialogRequest

sealed interface ProfileUiEvent {
    data  class Navigate(val screen : Screen) : ProfileUiEvent
    data object Logout : ProfileUiEvent
    data object DismissDialog : ProfileUiEvent
    data class ShowDialog(val dialog: DialogRequest<ProfileUiEvent>) : ProfileUiEvent
}