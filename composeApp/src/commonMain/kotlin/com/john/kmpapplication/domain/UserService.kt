package com.john.kmpapplication.domain

import com.john.kmpapplication.data.LoginRequest
import com.john.kmpapplication.data.SignUpRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
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

    suspend fun signUp(signUpRequest: SignUpRequest) =
        client.post("/api/v1/users/") {
            contentType(ContentType.Application.Json)
            setBody(signUpRequest)
        }

    suspend fun uploadFile(imageBytes: ByteArray) = client.post("/api/v1/files/upload") {
        setBody(
            MultiPartFormDataContent(
                formData {
                    append("file", imageBytes, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"avatar.jpg\"")
                    })
                }
            )
        )
    }
}
