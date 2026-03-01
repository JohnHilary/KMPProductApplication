package com.john.kmpapplication.ui.product

sealed interface ProductDetailUiEffect {
    data class ShowSnackbar(val message: String) : ProductDetailUiEffect
    data object NavigateBack : ProductDetailUiEffect
}