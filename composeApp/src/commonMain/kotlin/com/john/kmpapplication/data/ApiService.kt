package com.john.kmpapplication.data

import io.ktor.client.HttpClient
import io.ktor.client.request.get

class ApiService(
    private val client: HttpClient
) {
    suspend fun getProducts() =
        client.get("/products")
}