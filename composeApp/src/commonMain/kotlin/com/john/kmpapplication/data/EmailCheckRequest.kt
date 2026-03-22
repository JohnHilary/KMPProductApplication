package com.john.kmpapplication.data

import kotlinx.serialization.Serializable

@Serializable
data class EmailCheckRequest(
    val email: String
)