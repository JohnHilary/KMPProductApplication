package com.john.kmpapplication


interface TokenStorage {
    suspend fun saveAccessToken(token: String)
    suspend fun getAccessToken(): String?
    suspend fun clearToken()
    suspend fun saveRefreshToken(token: String)
    suspend fun getRefreshToken(): String?
}
