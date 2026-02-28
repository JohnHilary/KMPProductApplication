package com.john.kmpapplication.domain

import io.ktor.client.HttpClient
import io.ktor.client.request.get

class ApiService(
    private val client: HttpClient
) {
    suspend fun getProducts() =
        client.get("/products")
}