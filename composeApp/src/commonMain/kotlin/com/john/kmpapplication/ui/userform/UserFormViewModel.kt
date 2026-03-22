package com.john.kmpapplication.ui.userform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.kmpapplication.data.EmailCheckRequest
import com.john.kmpapplication.data.EmailCheckResponse
import com.john.kmpapplication.data.UserRequest
import com.john.kmpapplication.data.remote.ApiResult
import com.john.kmpapplication.domain.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserFormViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UserFormUiState())

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = userRepository.getUserFlow()
        .take(1)
        .map { user ->
            if (user == null) {
                UserFormUiState()
            } else {
                UserFormUiState(
                    isLoading = false,
                    userId = user.id,
                    username = user.username,
                    email = user.email,
                    image = user.avatar,
                    password = user.password
                )
            }
        }
        .flatMapLatest { initialState ->
            _uiState.value = initialState
            _uiState
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserFormUiState(isLoading = true)
        )
    private val _uiEffect = Channel<UserFormUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
       // observeEmail()
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeEmail() {
        uiState
            .map { it.email }
            .debounce(300)
            .distinctUntilChanged()
            .onEach { email ->
                if (email.isNotBlank()) setLoading(true)
            }
            .map { email ->
                if (email.isBlank()) null else checkIfEmailExists(email)
            }
            .flowOn(Dispatchers.Default)
            .onEach { result ->
                setLoading(false)
                if (result == null) return@onEach
                _uiState.update { state ->
                    when (result) {
                        is ApiResult.Success -> {
                            val isAvailable = result.data.isAvailable
                            state.copy(emailError = if (isAvailable) null else "Email is already in use.")
                        }
                        is ApiResult.Error -> {
                            state.copy(emailError = result.message)
                        }
                        is ApiResult.Exception -> {
                            state.copy(emailError = null)
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }


    fun onEvent(event: UserFormUiEvent) {
        when (event) {
            is UserFormUiEvent.OnEmailChanged -> onEmailChanged(event.email)
            UserFormUiEvent.OnLoginButtonClick -> {
                viewModelScope.launch {
                    _uiEffect.send(UserFormUiEffect.NavigateToLogin)
                }
            }

            is UserFormUiEvent.OnPasswordChanged -> onPasswordChanged(event.password)

            is UserFormUiEvent.OnUsernameChanged -> onUsernameChanged(event.username)

            is UserFormUiEvent.OnImageUploadClicked -> uploadImage(event.image)
            is UserFormUiEvent.OnSubmitClick -> validateAndSubmit(submitType = event.submitType)
        }
    }

    private fun validateAndSubmit(submitType: SubmitType) {
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
        when (submitType) {
            SubmitType.SIGNUP -> signUp()
            SubmitType.UPDATE -> updateUser()
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            try {
                setLoading(true)
                val userRequest = UserRequest(
                    avatar = _uiState.value.image,
                    name = _uiState.value.username,
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )
                when (val response = userRepository.signUp(userRequest)) {
                    is ApiResult.Error -> throw Exception(response.message)
                    is ApiResult.Exception -> throw response.throwable
                    is ApiResult.Success -> {
                        userRepository.insertUser(userResponse = response.data)
                        setLoading(false)
                        _uiEffect.send(UserFormUiEffect.NavigateToProfile)
                    }
                }
            } catch (e: Exception) {
                setLoading(false)
                _uiEffect.send(
                    UserFormUiEffect.ShowSnackbar(
                        e.message ?: "Something went wrong",
                    )
                )
            }
        }
    }

    private fun uploadImage(image: ByteArray?) {
        viewModelScope.launch {
            try {
                if (image == null) {
                    _uiEffect.send(
                        UserFormUiEffect.ShowSnackbar("Image is required")
                    )
                    return@launch
                }
                setLoading(true)
                when (val response = userRepository.uploadFile(image)) {
                    is ApiResult.Error -> throw Exception(response.message)
                    is ApiResult.Exception -> throw response.throwable
                    is ApiResult.Success -> {
                        onImageChange(image = response.data.location)
                        setLoading(false)
                    }
                }
            } catch (e: Exception) {
                setLoading(false)
                _uiEffect.send(
                    UserFormUiEffect.ShowSnackbar(
                        e.message ?: "Something went wrong",
                    )
                )
            }
        }
    }


    private fun onImageChange(image: String) {
        _uiState.update {
            it.copy(image = image)
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

    private fun onUsernameChanged(username: String) {
        _uiState.update {
            it.copy(username = username, usernameError = null)
        }
    }

    private suspend fun checkIfEmailExists(email: String): ApiResult<EmailCheckResponse> {
        val checkRequest = EmailCheckRequest(email = email)
        return userRepository.checkIfEmailExists(checkRequest)
    }

    private fun updateUser() {
        viewModelScope.launch {
            setLoading(true)

            val userRequest = UserRequest(
                _uiState.value.image,
                _uiState.value.email,
                _uiState.value.username,
                _uiState.value.password
            )
            try {
                when (val response =
                    userRepository.updateUser(id = _uiState.value.userId, userRequest = userRequest)) {
                    is ApiResult.Error -> throw Exception(response.message)
                    is ApiResult.Exception -> throw response.throwable
                    is ApiResult.Success -> {
                        userRepository.insertUser(userResponse = response.data)
                        setLoading(false)
                        _uiEffect.send(UserFormUiEffect.NavigateToProfile)
                    }
                }
            } catch (e: Exception) {
                setLoading(false)
                _uiEffect.send(
                    UserFormUiEffect.ShowSnackbar(
                        e.message ?: "Something went wrong",
                    )
                )
            }
        }
    }

}