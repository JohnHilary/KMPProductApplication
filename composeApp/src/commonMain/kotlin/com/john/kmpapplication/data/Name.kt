package com.john.kmpapplication.data

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    val firstname: String,
    val lastname: String
)