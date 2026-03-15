package com.john.kmpapplication.ui.profile

import com.john.kmpapplication.data.User

data class ProfileUiState(val isLoading: Boolean = false, val user: User? = null, val isLoggedIn: Boolean = false)
