package com.john.kmpapplication.ui.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.kmpapplication.domain.UserRepository
import com.john.kmpapplication.ui.component.dialog.DialogHostState
import com.john.kmpapplication.ui.component.dialog.DialogResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = userRepository.getUserFlow()
        .map { user ->
            ProfileUiState(
                isLoading = false,
                userEntity = user,
                isLoggedIn = user != null
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileUiState(isLoading = true)
        )
    private val _uiEffect = Channel<ProfileUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    val dialogState = DialogHostState()

    fun onEvent(uiEvent: ProfileUiEvent) {
        when (uiEvent) {
            is ProfileUiEvent.LogoutIconClicked -> {
                viewModelScope.launch {
                    val result = dialogState.showDialog(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        title = "Logout",
                        message = "Are you sure you want to log out?",
                        positiveButton = "Cancel",
                        negativeButton = "Logout"
                    )

                    if (result == DialogResult.Negative) {
                        logout()
                    }
                }
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    private fun logout() {
        viewModelScope.launch {
            setLoading(true)
            delay(1000)
            userRepository.deleteUser()
            setLoading(false)
            dialogState.showDialog(
                icon = Icons.Default.CheckCircle,
                title = "Success",
                message = "Successfully logged out",
                positiveButton = "Ok",
            )
        }
    }


}