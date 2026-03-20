package com.john.kmpapplication.ui.component.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.kmpapplication.data.SignUpRequest
import com.john.kmpapplication.data.remote.ApiResult
import com.john.kmpapplication.domain.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()
    private val _uiEffect = Channel<SignUpUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()


    private fun setLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }


    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.OnEmailChanged -> onEmailChanged(event.email)
            SignUpUiEvent.OnLoginButtonClick -> {
                viewModelScope.launch {
                    _uiEffect.send(SignUpUiEffect.NavigateToLogin)
                }
            }

            is SignUpUiEvent.OnPasswordChanged -> onPasswordChanged(event.password)

            is SignUpUiEvent.OnUsernameChanged -> onUsernameChanged(event.username)
            SignUpUiEvent.OnSignUpButtonClick -> validateAndSignUp()
        }
    }

    private fun validateAndSignUp() {
        var isValid = true
        var usernameError: String? = null
        var emailError: String? = null
        var passwordError: String? = null

        if (_uiState.value.username.isBlank()) {
            usernameError = "Username is empty"
            isValid = false
        }

        if (_uiState.value.email.isBlank()) {
            emailError = "Email is empty"
            isValid = false
        }

        if (_uiState.value.password.isBlank()) {
            passwordError = "Password is empty"
            isValid = false
        }

        _uiState.value = _uiState.value.copy(
            usernameError = usernameError,
            emailError = emailError,
            passwordError = passwordError
        )
        if (!isValid) return
        signUp()
    }

    private fun signUp() {
        viewModelScope.launch {
            try {
                setLoading(true)
                val signUpRequest = SignUpRequest(
                    avatar = _uiState.value.image,
                    name = _uiState.value.username,
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )
                when (val response = userRepository.signUp(signUpRequest)) {
                    is ApiResult.Error -> throw Exception(response.message)
                    is ApiResult.Exception -> throw response.throwable
                    is ApiResult.Success -> {
                        userRepository.insertUser(profileResponse = response.data)
                        setLoading(false)
                        _uiEffect.send(SignUpUiEffect.NavigateToProfile)
                    }
                }
            } catch (e: Exception) {
                setLoading(false)
                _uiEffect.send(
                    SignUpUiEffect.ShowSnackbar(
                        e.message ?: "Something went wrong",
                    )
                )
            }
        }
    }


    private fun onEmailChanged(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    private fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    private fun onUsernameChanged(username: String) {
        _uiState.update {
            it.copy(username = username)
        }
    }
}