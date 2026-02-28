package com.john.kmpapplication

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform