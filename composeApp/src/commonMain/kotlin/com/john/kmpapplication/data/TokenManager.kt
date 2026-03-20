package com.john.kmpapplication.data

import com.john.kmpapplication.TokenStorage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


class TokenManager(private val storage: TokenStorage) {

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    suspend fun triggerLogout() {
        _logoutEvent.emit(Unit)
    }

    suspend fun saveAccessToken(token: String) {
        storage.saveAccessToken(token)
    }

    suspend fun getAccessToken(): String? {
        return storage.getAccessToken()
    }

    suspend fun saveRefreshToken(token: String) {
        storage.saveRefreshToken(token)
    }

    suspend fun getRefreshToken(): String? {
        return storage.getRefreshToken()
    }

    suspend fun clearToken() {
        storage.clearToken()
    }
}