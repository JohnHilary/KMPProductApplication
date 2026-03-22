package com.john.kmpapplication.data

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: String,
    val message: List<String>,
    val statusCode: Int
)