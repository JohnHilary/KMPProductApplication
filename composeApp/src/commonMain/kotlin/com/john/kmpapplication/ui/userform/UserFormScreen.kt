package com.john.kmpapplication.ui.userform

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
data class UserFormScreen(val type: Int = SubmitType.SIGNUP.value)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserFormScreen(
    navController: NavController,
    uiState: UserFormUiState,
    uiEffect: Flow<UserFormUiEffect>?,
    submitType: SubmitType = SubmitType.SIGNUP,
    onEvent: (UserFormUiEvent) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var passwordVisible by retain { mutableStateOf(false) }
    var showImageDialog by retain { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val picker = LocalImagePicker.current



    LaunchedEffect(Unit) {
        uiEffect?.collect { effect ->
            when (effect) {
                UserFormUiEffect.NavigateToLogin -> navController.navigate(LoginScreen) {
                    popUpTo(UserFormScreen()) {
                        inclusive = true
                    }
                }

                is UserFormUiEffect.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = effect.message,
                        actionLabel = effect.actionLabel
                    )

                }

                UserFormUiEffect.NavigateBack -> navController.navigateUp()
                UserFormUiEffect.NavigateToProfile -> {
                    navController.navigate(MyProfile) {
                        popUpTo(UserFormScreen()) {
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
                text = when (submitType) {
                    SubmitType.SIGNUP -> "Create Account"
                    SubmitType.UPDATE -> "Update Account"
                },
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
                            onValueChange = { onEvent(UserFormUiEvent.OnUsernameChanged(it)) },
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
                            onValueChange = { onEvent(UserFormUiEvent.OnEmailChanged(it)) },
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
                            onValueChange = { onEvent(UserFormUiEvent.OnPasswordChanged(it)) },
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
                            onClick = { onEvent(UserFormUiEvent.OnSubmitClick(submitType = submitType)) },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        ) {
                            Text(
                                when (submitType) {
                                    SubmitType.SIGNUP -> "Register"
                                    SubmitType.UPDATE -> "Update"
                                }
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        if (submitType == SubmitType.SIGNUP ) {
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
                                                onEvent(UserFormUiEvent.OnLoginButtonClick)
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
                                onEvent(UserFormUiEvent.OnImageUploadClicked(it?.copyOf()))
                            })
                    },
                    onCameraSelect = {
                        picker.pickImage(
                            type = PickerType.CAMERA,
                            onResult = {
                                showImageDialog = false
                                onEvent(UserFormUiEvent.OnImageUploadClicked(it?.copyOf()))
                            })

                    })
            }
        }
    }
}


enum class SubmitType(val value: Int) {
    SIGNUP(1),
    UPDATE(2);

    companion object {
        fun from(value: Int) = entries.find { it.value == value } ?: SIGNUP
    }
}