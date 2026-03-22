package com.john.kmpapplication.data.remote

import com.john.kmpapplication.data.ErrorResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

suspend inline fun <reified T> handleApi(
    crossinline execute: suspend () -> HttpResponse
): ApiResult<T> {
    return try {
        val response = execute()
        val responseBody = response.bodyAsText()
        if (response.status.isSuccess()) {
            val body = response.body<T>()
            ApiResult.Success(body)
        } else {
            val errorMessage = try {
                val errorObj = Json.decodeFromString<ErrorResponse>(responseBody)
                errorObj.message.firstOrNull() ?: "Unknown error"
            } catch (e: Exception) {
                responseBody.ifEmpty { "Error ${e.message}" }
            }
            ApiResult.Error(
                code = response.status.value,
                message = errorMessage
            )
        }
    } catch (e: Throwable) {
        ApiResult.Exception(e)
    }
}