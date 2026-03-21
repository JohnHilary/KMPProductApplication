package com.john.kmpapplication.ui.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.retain.retain
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
import com.john.kmpapplication.LocalImagePicker
import com.john.kmpapplication.PickerType
import com.john.kmpapplication.ui.BaseScreen
import com.john.kmpapplication.ui.component.AppImage
import com.john.kmpapplication.ui.component.FullScreenLoader
import com.john.kmpapplication.ui.component.dialog.ImageSourceDialog
import com.john.kmpapplication.ui.login.LoginScreen
import com.john.kmpapplication.ui.profile.MyProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable


@Serializable
data object SignUpScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    uiState: SignUpUiState,
    uiEffect: Flow<SignUpUiEffect>?,
    onEvent: (SignUpUiEvent) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var passwordVisible by retain { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var showImageDialog by retain { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val picker = LocalImagePicker.current



    LaunchedEffect(uiEffect) {
        uiEffect?.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )?.collect { effect ->
            when (effect) {
                SignUpUiEffect.NavigateToLogin -> navController.navigate(LoginScreen) {
                    popUpTo(SignUpScreen) {
                        inclusive = true
                    }
                }

                is SignUpUiEffect.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = effect.message,
                        actionLabel = effect.actionLabel
                    )

                }

                SignUpUiEffect.NavigateBack -> navController.navigateUp()
                SignUpUiEffect.NavigateToProfile -> {
                    navController.navigate(MyProfile) {
                        popUpTo(SignUpScreen) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }


    BaseScreen(
        snackbarHostState = snackbarHostState,
        scrollBehavior = scrollBehavior,
        title = {
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

        },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            Column {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.40f)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            AppImage(
                                modifier = Modifier.align(Alignment.Center),
                                imageUrl = uiState.image,
                                shape = CircleShape,
                                defaultIcon = Icons.Filled.Person,
                                shadowElevation = 8.dp,
                                backgroundColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                            Surface(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(36.dp)
                                    .offset(x = (-8).dp, y = (-8).dp),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                tonalElevation = 4.dp,
                                onClick = { showImageDialog = true }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddAPhoto,
                                    contentDescription = "Upload Image",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth().weight(0.60f),
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {


                        OutlinedTextField(
                            value = uiState.username,
                            onValueChange = { onEvent(SignUpUiEvent.OnUsernameChanged(it)) },
                            label = { Text("Username") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            maxLines = 1,
                            supportingText = {
                                if (uiState.usernameError != null) {
                                    Text(
                                        text = uiState.usernameError,
                                        color = Color.Red,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            )

                        OutlinedTextField(
                            value = uiState.email,
                            onValueChange = { onEvent(SignUpUiEvent.OnEmailChanged(it)) },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            maxLines = 1,
                            supportingText = {
                                if (uiState.emailError != null) {
                                    Text(
                                        text = uiState.emailError,
                                        color = Color.Red,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        )

                        OutlinedTextField(
                            value = uiState.password,
                            onValueChange = { onEvent(SignUpUiEvent.OnPasswordChanged(it)) },
                            label = { Text("Password") },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            maxLines = 1,
                            trailingIcon = {
                                val image = if (passwordVisible)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff

                                val description = if (passwordVisible) "Hide password" else "Show password"

                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(imageVector = image, contentDescription = description)
                                }
                            },
                            supportingText = {
                                if (uiState.passwordError != null) {
                                    Text(
                                        text = uiState.passwordError,
                                        color = Color.Red,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                        )

                        Button(
                            onClick = { onEvent(SignUpUiEvent.OnSignUpButtonClick) },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        ) {
                            Text("Register")
                        }
                        Spacer(Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val annotatedText = buildAnnotatedString {
                                append("Already have an account? ")
                                pushLink(
                                    LinkAnnotation.Clickable(
                                        tag = "login",
                                        linkInteractionListener = {
                                            onEvent(SignUpUiEvent.OnLoginButtonClick)
                                        }
                                    )
                                )
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("Log in")
                                }
                                pop()
                            }
                            Text(text = annotatedText)
                        }
                    }
                }
            }
            FullScreenLoader(isLoading = uiState.isLoading)
            if (showImageDialog) {
                ImageSourceDialog(
                    onDismiss = {
                        showImageDialog = false
                    }, onGallerySelect = {
                        picker.pickImage(
                            type = PickerType.GALLERY,
                            onResult = {
                                showImageDialog = false
                                onEvent(SignUpUiEvent.OnImageUploadClicked(it?.copyOf()))
                            })
                    },
                    onCameraSelect = {
                        picker.pickImage(
                            type = PickerType.CAMERA,
                            onResult = {
                                showImageDialog = false
                                onEvent(SignUpUiEvent.OnImageUploadClicked(it?.copyOf()))
                            })

                    })
            }
        }
    }
}
