package com.john.kmpapplication.domain

import com.john.kmpapplication.data.LoginRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class UserService(
    private val client: HttpClient
) {
    suspend fun login(username: String, password: String) =
        client.post("auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(username, password))
        }

    suspend fun getUser(id: Int?) = client.get("users/$id")

}