package com.john.kmpapplication.data

import kotlinx.serialization.Serializable

@Serializable
data class FileUploadResponse(
    val filename: String,
    val location: String,
    val originalname: String
)