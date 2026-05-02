package com.john.kmpapplication.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.john.kmpapplication.ui.BaseScreen
import com.john.kmpapplication.ui.component.AppImage
import com.john.kmpapplication.ui.component.FullScreenLoader
import com.john.kmpapplication.ui.component.dialog.AppDialog
import com.john.kmpapplication.ui.component.dialog.DialogState
import com.john.kmpapplication.ui.login.LoginScreen
import com.john.kmpapplication.ui.navigation.AnimatedBottomBar
import com.john.kmpapplication.ui.userform.SubmitType
import com.john.kmpapplication.ui.userform.UserFormScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable


@Serializable
data object MyProfile {}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    navController: NavController, uiEffect: Flow<ProfileUiEffect>? = null,
    uiState: ProfileUiState = ProfileUiState(),
    dialogState: DialogState? = null,
    onEvent: (ProfileUiEvent) -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        uiEffect?.collect { effect ->
            when (effect) {
                is ProfileUiEffect.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = effect.message,
                        actionLabel = effect.actionLabel
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                    }
                }

                ProfileUiEffect.NavigateToUserFormScreen -> navController.navigate(UserFormScreen(type = SubmitType.UPDATE.value))
            }
        }
    }



    BaseScreen(
        snackbarHostState = snackbarHostState,
       topBarColors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
        ), title = {}, actions = {
            if (uiState.isLoggedIn) {
                Surface(
                    modifier = Modifier.padding(end = 16.dp).size(56.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shadowElevation = 8.dp
                ) {
                    IconButton(onClick = {
                        onEvent(ProfileUiEvent.LogoutIconClicked)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout"
                        )
                    }
                }
            }
        }, bottomBar = {
            AnimatedBottomBar(
                navController = navController
            )
        }) {
        Box(
            Modifier.fillMaxSize().padding(
                top = (it.calculateTopPadding() + 16.dp),
                start = 16.dp,
                end = 16.dp,
                bottom = (it.calculateBottomPadding() + 16.dp)
            )
        ) {
            if (uiState.isLoggedIn && !uiState.isLoading) {
                uiState.userEntity?.let { user ->

                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(

                        ), horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Spacer(modifier = Modifier.height(12.dp))
                        AppImage(
                            imageUrl = user.avatar,
                            shape = CircleShape,
                            defaultIcon = Icons.Filled.Person,
                            shadowElevation = 8.dp,
                            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = user.username,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = user.email, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                    }


                    Card(
                        shape = RoundedCornerShape(
                            topStart = 16.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 0.dp
                        ), colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ), modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                    ) {

                        ProfileItem(
                            modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
                            icon = Icons.Filled.Person,
                            title = user.username,
                            description = "@Test",
                            onClick = {
                                 onEvent(ProfileUiEvent.NavigateToUserFormScreen)
                            })
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        ProfileItem(
                            modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
                            icon = Icons.Filled.Notifications,
                            title = "Notifications",
                            description = "View Notifications",
                            onClick = {

                            })
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        ProfileItem(
                            modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
                            icon = Icons.Filled.Settings,
                            title = "Settings",
                            description = "Security,Privacy",
                            onClick = {

                            })


                    }
                }
            } else {
                if (!uiState.isLoading) {
                    NotLoggedInUI {
                        navController.navigate(LoginScreen)
                    }
                }
            }

            AppDialog(dialogState = dialogState)
            FullScreenLoader(isLoading = uiState.isLoading)
        }
    }
}

@Composable
fun ProfileItem(modifier: Modifier, icon: ImageVector, title: String, description: String, onClick: () -> Unit) {
    Surface(onClick = onClick, color = MaterialTheme.colorScheme.primaryContainer) {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            AppImage(
                imageUrl = "",
                size = 60.dp,
                shape = CircleShape,
                defaultIcon = icon,
                shadowElevation = 8.dp,
                backgroundColor = MaterialTheme.colorScheme.primaryContainer
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight, contentDescription = "Logout"
            )
        }

    }
}