package com.john.kmpapplication.util

object AppUtils {

   private val EMAIL_REGEX = Regex(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    fun String.isValidEmail(): Boolean {
        return this.isNotBlank() && EMAIL_REGEX.matches(this)
    }
}