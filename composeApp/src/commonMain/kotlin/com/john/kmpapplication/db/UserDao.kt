package com.john.kmpapplication.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    @Query("SELECT * FROM user")
    fun getUser():  Flow<UserEntity?>

    @Query("DELETE FROM user")
    suspend fun deleteUser()
}