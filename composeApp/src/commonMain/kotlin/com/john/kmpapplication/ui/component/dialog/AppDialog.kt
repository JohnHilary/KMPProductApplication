package com.john.kmpapplication.ui.component.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppDialog(
    dialogState: DialogState?
) {
    dialogState?.currentDialogData?.value?.let { data ->
        val visuals = data.visuals
        AlertDialog(
            onDismissRequest = data::onPositive,
            icon = {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = visuals.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = visuals.title,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    modifier = Modifier.fillMaxWidth(), text = visuals.message, fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(onClick =data::onPositive) {
                    Text(text = visuals.positiveButton, fontSize = 14.sp)
                }
            },
            dismissButton = {
                visuals.negativeButton?.let {
                    TextButton(onClick = data::onNegative) {
                        Text(text = it, fontSize = 14.sp)
                    }
                }
            }
        )
    }
}