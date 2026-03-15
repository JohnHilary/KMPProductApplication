package com.john.kmpapplication.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JwtPayload(
    @SerialName("sub") val userId: Int,
    val email: String? = null,
    val iat: Long? = null,
    val exp: Long? = null,
    val roles: List<String> = emptyList()
)
