package com.john.kmpapplication.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.john.kmpapplication.util.DatabaseConstant

@Entity(tableName = DatabaseConstant.USER_TABLE)
data class UserEntity(
    @PrimaryKey val id: Int,
    val email: String,
    val username: String,
    val phone: String,
    @Embedded val name: NameEntity,
    @Embedded val address: AddressEntity
)
