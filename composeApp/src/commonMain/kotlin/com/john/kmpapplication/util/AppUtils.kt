package com.john.kmpapplication.util


object AppUtils {
    fun String.isValidName(): Boolean {
        return this.isNotBlank() && this.matches(Regex("^[a-zA-Z ]{2,}$"))
    }

}