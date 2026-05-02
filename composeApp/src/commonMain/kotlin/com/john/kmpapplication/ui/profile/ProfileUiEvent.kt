package com.john.kmpapplication.ui.profile

sealed interface ProfileUiEvent {
    data object LogoutClicked : ProfileUiEvent
    data object NavigateToUserFormScreen : ProfileUiEvent

}