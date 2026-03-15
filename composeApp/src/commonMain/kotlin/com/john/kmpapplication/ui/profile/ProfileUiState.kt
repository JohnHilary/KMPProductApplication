package com.john.kmpapplication.ui.profile

import com.john.kmpapplication.db.UserEntity

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userEntity: UserEntity? = null,
    val isLoggedIn: Boolean = false
)
