package com.john.kmpapplication.data

import com.john.kmpapplication.db.AddressEntity
import com.john.kmpapplication.db.GeoEntity
import com.john.kmpapplication.db.NameEntity
import com.john.kmpapplication.db.UserEntity
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val __v: Int,
    val address: Address,
    val email: String,
    val id: Int,
    val name: Name,
    val password: String,
    val phone: String,
    val username: String
)

fun UserResponse.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        email = this.email,
        username = this.username,
        phone = this.phone,
        name = NameEntity(
            firstname = this.name.firstname,
            lastname = this.name.lastname
        ),
        address = AddressEntity(
            city = this.address.city,
            street = this.address.street,
            number = this.address.number,
            zipcode = this.address.zipcode,
            geolocation = GeoEntity(
                lat = this.address.geolocation.lat,
                long = this.address.geolocation.long
            )
        )
    )
}