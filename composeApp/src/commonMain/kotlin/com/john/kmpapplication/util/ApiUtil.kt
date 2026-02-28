package com.john.kmpapplication.util

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

suspend inline fun <reified T> handleApi(
    crossinline execute: suspend () -> HttpResponse
): ApiResult<T> {
    return try {
        val response = execute()
        if (response.status.isSuccess()) {
            val body = response.body<T>()
            ApiResult.Success(body)
        } else {
            ApiResult.Error(
                code = response.status.value,
                message = response.bodyAsText().ifEmpty { "Network error" }
            )
        }
    } catch (e: Throwable) {
        ApiResult.Exception(e)
    }
}