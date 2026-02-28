package com.john.kmpapplication.ui.product

sealed interface ProductUiEffect {
    data class ShowSnackbar(val message: String) : ProductUiEffect
    data object NavigateBack : ProductUiEffect
}