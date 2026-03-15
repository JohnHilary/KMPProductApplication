package com.john.kmpapplication.ui.profile

import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.john.kmpapplication.ui.BaseScreen
import com.john.kmpapplication.ui.component.AppImage
import com.john.kmpapplication.ui.navigation.AnimatedBottomBar
import kotlinx.serialization.Serializable


@Serializable
data object MyProfile {}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(navController: NavController, uiState: ProfileUiState = ProfileUiState()) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    BaseScreen(
        scrollBehavior = scrollBehavior, topBarColors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
        ), title = {}, actions = {
            Surface(
                modifier = Modifier.padding(end = 16.dp).size(56.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                shadowElevation = 8.dp
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout"
                    )
                }
            }
        }, bottomBar = {
            AnimatedBottomBar(
                navController = navController
            )
        }) {
        uiState.user?.let { user ->
            Box(
                Modifier.fillMaxSize().padding(
                    top = (it.calculateTopPadding() + 16.dp),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = (it.calculateBottomPadding() + 16.dp)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(

                    ), horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(12.dp))
                    AppImage(
                        imageUrl = "",
                        size = 160.dp,
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
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.ChevronRight, contentDescription = "Logout"
                )
            }
        }
    }
}