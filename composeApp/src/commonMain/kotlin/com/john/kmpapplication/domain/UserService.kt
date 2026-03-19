package com.john.kmpapplication.domain

import com.john.kmpapplication.data.LoginRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class UserService(
    private val client: HttpClient,
) {
    suspend fun login(email: String, password: String) =
        client.post ("/api/v1/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }

    suspend fun getProfile() = client.get("/api/v1/auth/profile")

}