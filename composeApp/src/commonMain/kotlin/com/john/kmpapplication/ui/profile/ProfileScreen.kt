package com.john.kmpapplication.ui.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.john.kmpapplication.ui.BaseScreen
import com.john.kmpapplication.ui.navigation.AnimatedBottomBar
import kotlinx.serialization.Serializable


@Serializable
data object MyProfile {}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    BaseScreen(
        scrollBehavior = scrollBehavior,
        title = {
            Text(text = "My profile", fontWeight = FontWeight.Bold)
        },
        bottomBar = {
            AnimatedBottomBar(
                navController = navController
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
            ) {
                Icon(Icons.Filled.Add, "Add")
            }
        }
    ) {

    }
}