package com.john.kmpapplication

import platform.Foundation.NSUserDefaults

class IosTokenStorage : TokenStorage {

    private val defaults = NSUserDefaults.standardUserDefaults
    private val authTokenKey = "auth_token"
    private val refreshTokenKey = "refresh_token"

    override suspend fun saveAccessToken(token: String) {
        defaults.setObject(token, authTokenKey)
    }

    override suspend fun getAccessToken(): String? {
        return defaults.stringForKey(authTokenKey)
    }

    override suspend fun clearToken() {
        defaults.removeObjectForKey(authTokenKey)
    }

    override suspend fun saveRefreshToken(token: String) {
        defaults.setObject(token, refreshTokenKey)
    }

    override suspend fun getRefreshToken(): String? {
        return defaults.stringForKey(refreshTokenKey)
    }
}