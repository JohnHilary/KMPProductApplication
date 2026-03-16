package com.john.kmpapplication.ui.component.dialog

import androidx.compose.runtime.State
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume


@Stable
class DialogHostState : DialogState {
    private val mutex = Mutex()

    private val _currentDialogData = mutableStateOf<DialogData?>(null)
    override val currentDialogData = _currentDialogData

    suspend fun showDialog(
        icon: ImageVector,
        title: String,
        message: String,
        positiveButton: String,
        negativeButton: String? = null,
    ): DialogResult = mutex.withLock {
        suspendCancellableCoroutine { continuation ->
            val visuals = object : DialogVisuals {
                override val icon = icon
                override val title = title
                override val message = message
                override val positiveButton = positiveButton
                override val negativeButton = negativeButton
            }

            val dialogData = DialogDataImpl(visuals, continuation)
            _currentDialogData.value = dialogData
        }.also {
            dismiss()
        }
    }

    private class DialogDataImpl(
        override val visuals: DialogVisuals,
        private val continuation: CancellableContinuation<DialogResult>
    ) : DialogData {

        override fun onPositive() {
            if (continuation.isActive) continuation.resume(DialogResult.Positive)
        }

        override fun onNegative() {
            if (continuation.isActive) continuation.resume(DialogResult.Negative)
        }
    }

    fun dismiss() {
        _currentDialogData.value = null
    }
}

interface DialogState {
    val currentDialogData: State<DialogData?>
}