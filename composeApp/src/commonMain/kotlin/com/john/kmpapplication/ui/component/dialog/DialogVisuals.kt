package com.john.kmpapplication.ui.component.dialog

import androidx.compose.ui.graphics.vector.ImageVector

interface DialogVisuals {
    val icon : ImageVector
    val title: String
    val message: String
    val positiveButton: String
    val negativeButton: String?
}
