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
fun <T> AppDialog(
    dialogState: DialogRequest<T>?,
    onResult: (T?) -> Unit
) {
    dialogState?.let { data ->
        AlertDialog(
            onDismissRequest = { onResult.invoke(data.negativeResult) },
            icon = {
                data.icon?.let {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        imageVector = it,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            title = {
                data.title?.asString()?.let {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = it,
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                data.message?.asString()?.let {
                    Text(
                        modifier = Modifier.fillMaxWidth(), text = it, fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                Button(onClick = { onResult.invoke(data.positiveResult) }) {
                    data.positiveText?.let { Text(text = it.asString(), fontSize = 14.sp) }
                }
            },
            dismissButton = {
                data.negativeText?.let {
                    TextButton(onClick = { onResult.invoke(data.negativeResult) }) {
                        Text(text = it.asString(), fontSize = 14.sp)
                    }
                }
            }
        )
    }
}