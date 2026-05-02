package com.john.kmpapplication.ui.component.dialog

import androidx.compose.ui.graphics.vector.ImageVector
import com.john.kmpapplication.util.StringValue

interface DialogVisuals {
    val icon : ImageVector
    val title: StringValue
    val message: StringValue
    val positiveButton: StringValue
    val negativeButton: StringValue?
}
