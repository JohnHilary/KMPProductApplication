package com.john.kmpapplication.ui.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.kmpapplication.domain.UserRepository
import com.john.kmpapplication.ui.component.dialog.DialogRequest
import com.john.kmpapplication.ui.userform.SubmitType
import com.john.kmpapplication.ui.userform.UserFormScreen
import com.john.kmpapplication.util.StringValue.*
import kmpapplication.composeapp.generated.resources.Res
import kmpapplication.composeapp.generated.resources.cancel
import kmpapplication.composeapp.generated.resources.logout
import kmpapplication.composeapp.generated.resources.logout_message
import kmpapplication.composeapp.generated.resources.logout_success
import kmpapplication.composeapp.generated.resources.ok
import kmpapplication.composeapp.generated.resources.success
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

    init {
        observeLogoutEvent()
    }

    private fun observeLogoutEvent() {
        viewModelScope.launch {
            userRepository.sessionExpiredEvent.collect {
                userRepository.logout()
                _uiEffect.send(ProfileUiEffect.ShowSnackbar("Session Expired"))
            }
        }
    }

    fun onEvent(uiEvent: ProfileUiEvent) {
        when (uiEvent) {
            is ProfileUiEvent.LogoutClicked -> {
                    showDialog(
                        dialogRequest = DialogRequest(
                            icon = Icons.AutoMirrored.Filled.Logout,
                            title = StringRes(Res.string.logout),
                            message = StringRes(Res.string.logout_message),
                            positiveText = StringRes(Res.string.logout),
                            negativeText = StringRes(Res.string.cancel),
                            positiveResult = ProfileUiEvent.Logout,
                        )
                    )
            }
            ProfileUiEvent.NavigateToUserFormScreen -> {
                _uiEffect.trySend(ProfileUiEffect.Navigate(screen = UserFormScreen(type = SubmitType.UPDATE.value)))
            }
            ProfileUiEvent.Logout -> logout()
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    private fun logout() {
        viewModelScope.launch {
            setLoading(true)
            delay(1000)
            userRepository.logout()
            setLoading(false)
               showDialog(
                    dialogRequest = DialogRequest(
                        icon = Icons.Default.CheckCircle,
                        title = StringRes(Res.string.success),
                        message = StringRes(Res.string.logout_success),
                        positiveText = StringRes(Res.string.ok),
                    )
                )
        }
    }

    private fun showDialog(dialogRequest: DialogRequest<ProfileUiEvent>) {
        _uiEffect.trySend(
            ProfileUiEffect.ShowDialog(dialogRequest = dialogRequest)
        )
    }


}