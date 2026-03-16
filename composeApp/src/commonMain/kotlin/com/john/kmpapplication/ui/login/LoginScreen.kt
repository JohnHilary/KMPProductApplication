package com.john.kmpapplication.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.john.kmpapplication.ui.BaseScreen
import com.john.kmpapplication.ui.component.FullScreenLoader
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController = rememberNavController(),
    uiState: LoginUiState = LoginUiState(),
    uiEffect: Flow<LoginUiEffect>? = null,
    onEvent: (LoginUiEvent) -> Unit = {},
) {


    val lifecycleOwner = LocalLifecycleOwner.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    var passwordVisible by retain { mutableStateOf(false) }
    LaunchedEffect(uiEffect) {
        uiEffect?.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )?.collect { effect ->
            when (effect) {
                LoginUiEffect.NavigateToSignUp -> Unit
                is LoginUiEffect.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = effect.message,
                        actionLabel = effect.actionLabel
                    )

                }
                LoginUiEffect.NavigateBack -> navController.navigateUp()
            }
        }
    }


    BaseScreen(
        snackbarHostState = snackbarHostState,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = {
                navController.navigateUp()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Clear"
                )
            }
        }
    ) {

        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.45f)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(100.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "Welcome Back",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.55f),
                    shape = RoundedCornerShape(
                        topStart = 40.dp,
                        topEnd = 40.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            value = uiState.username,
                            onValueChange = {
                                onEvent(LoginUiEvent.OnUsernameChanged(it))
                            },
                            label = { Text("Username") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = uiState.usernameError != null,
                            supportingText = {
                                if (uiState.usernameError != null) {
                                    Text(
                                        text = uiState.usernameError,
                                        color = Color.Red,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            },
                            maxLines = 1
                        )
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = uiState.password,
                            onValueChange = {
                                onEvent(LoginUiEvent.OnPasswordChanged(it))

                            },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = uiState.passwordError != null,
                            supportingText = {
                                if (uiState.passwordError != null) {
                                    Text(
                                        text = uiState.passwordError,
                                        color = Color.Red,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            maxLines = 1,
                            trailingIcon = {
                                val image = if (passwordVisible)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff

                                val description = if (passwordVisible) "Hide password" else "Show password"

                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(imageVector = image, contentDescription = description)
                                }
                            }
                        )
                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = {
                                onEvent(
                                    LoginUiEvent.OnLoginButtonClick(
                                        username = uiState.username,
                                        password = uiState.password
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Login")
                        }
                        Spacer(Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val annotatedText = buildAnnotatedString {
                                append("Don't have an account? ")
                                pushLink(
                                    LinkAnnotation.Clickable(
                                        tag = "signup",
                                        linkInteractionListener = { onEvent(LoginUiEvent.OnSignUpButtonClick) }
                                    )
                                )
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("Sign up")
                                }
                                pop()
                            }
                            Text(text = annotatedText)
                        }
                    }
                }
            }
            FullScreenLoader(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)),
                isLoading = uiState.isLoading
            )

        }
    }
}

@Serializable
data object LoginScreen