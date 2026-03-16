package com.john.kmpapplication.ui.profile

sealed interface ProfileUiEvent {
    data object LogoutIconClicked : ProfileUiEvent
}