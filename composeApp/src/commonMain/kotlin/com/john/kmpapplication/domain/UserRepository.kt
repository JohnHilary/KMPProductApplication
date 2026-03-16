package com.john.kmpapplication.domain

import com.john.kmpapplication.data.LoginResponse
import com.john.kmpapplication.data.UserResponse
import com.john.kmpapplication.data.remote.ApiResult
import com.john.kmpapplication.data.remote.handleApi
import com.john.kmpapplication.data.toEntity
import com.john.kmpapplication.db.UserDao
import com.john.kmpapplication.db.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userService: UserService, private val userDao: UserDao) {

    suspend fun login(username: String, password: String): ApiResult<LoginResponse> {
        return handleApi {
            userService.login(username, password)
        }
    }

    suspend fun getUser(id: Int?): ApiResult<UserResponse> {
        return handleApi {
            userService.getUser(id)
        }
    }

    fun getUserFlow(): Flow<UserEntity?> {
        return userDao.getUser()
    }

    suspend fun insertUser(userResponse: UserResponse) {
        val entity = userResponse.toEntity()
        userDao.insertUser(entity)
    }

    suspend fun deleteUser() = userDao.deleteUser()
}