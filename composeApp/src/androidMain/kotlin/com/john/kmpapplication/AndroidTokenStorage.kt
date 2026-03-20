package com.john.kmpapplication

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class AndroidTokenStorage(
    private val dataStore: DataStore<Preferences>
) : TokenStorage {

    private val accessTokenKey = stringPreferencesKey("auth_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")

    override suspend fun saveAccessToken(token: String) {
        dataStore.edit { it[accessTokenKey] = token }
    }

    override suspend fun getAccessToken(): String? {
        return dataStore.data.first()[accessTokenKey]
    }

    override suspend fun clearToken() {
        dataStore.edit { it.remove(accessTokenKey) }
        dataStore.edit { it.remove(refreshTokenKey) }
    }

    override suspend fun saveRefreshToken(token: String) {
        dataStore.edit { it[refreshTokenKey] = token }
    }

    override suspend fun getRefreshToken(): String? {
        return dataStore.data.first()[refreshTokenKey]
    }
}

fun createDataStore(context: Context): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
        produceFile = { context.filesDir.resolve("token_storage.preferences_pb") }
    )
}