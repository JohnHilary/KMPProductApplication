package com.john.kmpapplication.util

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed class StringValue {

    data class StringRes(
        val res: StringResource,
    ) : StringValue()

    data class DynamicString(
        val value: String
    ) : StringValue()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringRes -> stringResource(res)
        }
    }

}
