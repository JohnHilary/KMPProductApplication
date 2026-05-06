package com.john.kmpapplication.ui.component

import com.john.kmpapplication.ui.userform.SubmitType
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {



    @Serializable
    data object ProductScreen: Screen()

    @Serializable
    data class ProductDetailScreen(val productId: Int?) : Screen()

    @Serializable
    data class UserFormScreen(val type: Int = SubmitType.SIGNUP.value) : Screen()

    @Serializable
    data object MyProfile : Screen()

    @Serializable
    data object LoginScreen : Screen()
}
