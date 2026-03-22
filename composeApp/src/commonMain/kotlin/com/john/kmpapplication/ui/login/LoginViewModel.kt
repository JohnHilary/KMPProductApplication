package com.john.kmpapplication.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.kmpapplication.data.LoginResponse
import com.john.kmpapplication.data.remote.ApiResult
import com.john.kmpapplication.domain.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    private val _uiEffect = Channel<LoginUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()


    private fun setLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnEmailChanged -> onEmailChanged(event.email)
            is LoginUiEvent.OnLoginButtonClick -> validateAndLogin(email = event.email, password = event.password)
            is LoginUiEvent.OnPasswordChanged -> onPasswordChanged(event.password)
            LoginUiEvent.OnSignUpButtonClick -> {
                viewModelScope.launch {
                    _uiEffect.send(LoginUiEffect.NavigateToSignUp)
                }
            }
        }

    }

    private fun onEmailChanged(email: String) {
        _uiState.update {
            it.copy(email = email, emailError = null)
        }
    }

    private fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(password = password, passwordError = null)
        }
    }

    private fun validateAndLogin(email: String, password: String) {
        var isValid = true
        var emailError: String? = null
        var passwordError: String? = null

        if (email.isBlank()) {
            emailError = "Email is empty"
            isValid = false
        }

        if (password.isBlank()) {
            passwordError = "Password is empty"
            isValid = false
        }

        _uiState.value = _uiState.value.copy(
            emailError = emailError,
            passwordError = passwordError
        )

        if (!isValid) return

        login(email, password)
    }


    private fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                setLoading(true)
                when (val result = repository.login(email, password)) {
                    is ApiResult.Error -> throw Exception(result.message)
                    is ApiResult.Exception -> throw result.throwable
                    is ApiResult.Success<LoginResponse> -> {
                        val tokens = result.data
                        repository.saveTokens(
                            accessToken = tokens.accessToken,
                            refreshToken = tokens.refreshToken
                        )
                        getProfile()

                    }
                }
            } catch (e: Exception) {
                setLoading(false)
                _uiEffect.send(
                    LoginUiEffect.ShowSnackbar(
                        e.message ?: "Something went wrong",
                    )
                )
            }
        }
    }

    suspend fun getProfile() {
        when (val result = repository.getProfile()) {
            is ApiResult.Error -> throw Exception(result.message)
            is ApiResult.Exception -> throw result.throwable
            is ApiResult.Success -> {
                repository.insertUser(profileResponse = result.data)
                setLoading(false)
                _uiEffect.send(LoginUiEffect.NavigateBack)
            }
        }
    }


}