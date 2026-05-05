package com.john.kmpapplication.ui.component.dialog

import androidx.compose.ui.graphics.vector.ImageVector
import com.john.kmpapplication.util.StringValue

data class DialogRequest<T>(
    val icon : ImageVector? = null,
    val title: StringValue? = null,
    val message: StringValue? = null,
    val positiveText: StringValue? = null,
    val negativeText: StringValue? = null,
    val positiveResult: T? = null,
    val negativeResult: T? = null
)
