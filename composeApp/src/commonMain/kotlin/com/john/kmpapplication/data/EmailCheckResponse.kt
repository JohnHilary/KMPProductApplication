package com.john.kmpapplication.data

import kotlinx.serialization.Serializable

@Serializable
data class EmailCheckResponse(
    val isAvailable: Boolean
)