package com.john.kmpapplication.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)
