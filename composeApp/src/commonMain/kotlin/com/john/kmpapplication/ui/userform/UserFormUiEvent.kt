package com.john.kmpapplication.ui.userform



sealed interface UserFormUiEvent {
    data class OnUsernameChanged(val username: String) : UserFormUiEvent
    data class OnEmailChanged(val email: String) : UserFormUiEvent
    data class OnPasswordChanged(val password: String) : UserFormUiEvent
    data class OnSubmitClick(val submitType: SubmitType) : UserFormUiEvent
    data object OnLoginButtonClick : UserFormUiEvent
    data class OnImageUploadClicked(val image: ByteArray?) : UserFormUiEvent
}