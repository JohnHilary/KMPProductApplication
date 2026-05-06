package com.john.kmpapplication.ui.profile

import com.john.kmpapplication.db.UserEntity
import com.john.kmpapplication.ui.component.dialog.DialogRequest

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userEntity: UserEntity? = null,
    val isLoggedIn: Boolean = false,
    val dialog: DialogRequest<ProfileUiEvent>? = null
)
