package com.john.kmpapplication.data

data class User(
    val email: String,
    val id: Int = 0,
    val password: String,
    val username: String
)
