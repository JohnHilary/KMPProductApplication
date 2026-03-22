package com.john.kmpapplication.ui.userform

data class UserFormUiState(
    val isLoading: Boolean = false,
    val userId: Int = -1,
    val image : String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
)
