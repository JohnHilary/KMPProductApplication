package com.john.kmpapplication.data

import kotlinx.serialization.Serializable


@Serializable
data class SignUpRequest(
    val avatar: String,
    val email: String,
    val name: String,
    val password: String
)