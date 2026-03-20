package com.john.kmpapplication.domain

import com.john.kmpapplication.data.LoginResponse
import com.john.kmpapplication.data.ProfileResponse
import com.john.kmpapplication.data.TokenManager
import com.john.kmpapplication.data.remote.ApiResult
import com.john.kmpapplication.data.remote.handleApi
import com.john.kmpapplication.data.toEntity
import com.john.kmpapplication.db.UserDao
import com.john.kmpapplication.db.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userService: UserService,
    private val userDao: UserDao,
    private val tokenManager: TokenManager
) {

    suspend fun login(email: String, password: String): ApiResult<LoginResponse> {
        return handleApi {
            userService.login(email, password)
        }
    }

    suspend fun getProfile(): ApiResult<ProfileResponse> {
        return handleApi {
            userService.getProfile()
        }
    }

    fun getUserFlow(): Flow<UserEntity?> {
        return userDao.getUser()
    }

    suspend fun insertUser(profileResponse: ProfileResponse) {
        val entity = profileResponse.toEntity()
        userDao.insertUser(entity)
    }

    suspend fun deleteUser() = userDao.deleteUser()

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        tokenManager.saveAccessToken(accessToken)
        tokenManager.saveRefreshToken(refreshToken)
    }

    suspend fun logout() {
        tokenManager.clearToken()
        userDao.deleteUser()
    }

    val sessionExpiredEvent = tokenManager.logoutEvent
}