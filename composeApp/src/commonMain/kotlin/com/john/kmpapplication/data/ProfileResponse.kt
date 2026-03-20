package com.john.kmpapplication.data

import com.john.kmpapplication.db.UserEntity
import kotlinx.serialization.Serializable


@Serializable
data class ProfileResponse(
    val avatar: String,
    val email: String,
    val id: Int,
    val name: String,
    val password: String,
    val role: String
)

fun ProfileResponse.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        email = this.email,
        username = this.name,
        role = this.role,
        password = this.password,
        avatar = this.avatar
    )
}
