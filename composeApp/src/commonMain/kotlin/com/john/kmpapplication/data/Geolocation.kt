package com.john.kmpapplication.data

import kotlinx.serialization.Serializable

@Serializable
data class Geolocation(
    val lat: String,
    val long: String
)