package com.john.kmpapplication.ui.component.signup

data class SignUpUiState(
    val isLoading: Boolean = false,
    val image : String = "https://picsum.photos/800",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
)
