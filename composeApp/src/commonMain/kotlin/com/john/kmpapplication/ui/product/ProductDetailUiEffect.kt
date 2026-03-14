package com.john.kmpapplication.ui.product

sealed interface ProductDetailUiEffect {
    data class ShowSnackbar(
        val message: String, val actionLabel: String? = null
    ) : ProductDetailUiEffect

    data object NavigateBack : ProductDetailUiEffect
}