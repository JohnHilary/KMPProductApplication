package com.john.kmpapplication.ui.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ImageSourceDialog(
    onDismiss: () -> Unit,
    onGallerySelect: () -> Unit,
    onCameraSelect: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Image Source") },
        text = { Text("Would you like to take a new photo or choose one from your gallery?") },
        confirmButton = {
            TextButton(onClick = onCameraSelect) { Text("Camera") }
        },
        dismissButton = {
            TextButton(onClick = onGallerySelect) { Text("Gallery") }
        }
    )
}