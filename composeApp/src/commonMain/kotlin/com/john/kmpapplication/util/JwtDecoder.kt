package com.john.kmpapplication.util

import com.john.kmpapplication.data.JwtPayload
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.decodeBase64

object JwtDecoder {
    private val json = Json { ignoreUnknownKeys = true }

    fun decodePayload(token: String): JwtPayload {
        val parts = token.split(".")
        require(parts.size == 3) { "Malformed JWT: Expected 3 parts" }

        val payload = parts[1]
        val paddedPayload = when (payload.length % 4) {
            2 -> "$payload=="
            3 -> "$payload="
            else -> payload
        }.replace('-', '+').replace('_', '/')

        val payloadBytes = paddedPayload.decodeBase64()
            ?: throw IllegalArgumentException("Failed to decode Base64 payload")

        return json.decodeFromString(payloadBytes.utf8())
    }
}