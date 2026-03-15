package com.john.kmpapplication.db

import androidx.room.Embedded

data class AddressEntity(
    val city: String,
    val street: String,
    val number: Int,
    val zipcode: String,
    @Embedded val geolocation: GeoEntity
)
data class GeoEntity(
    val lat: String,
    val long: String
)
