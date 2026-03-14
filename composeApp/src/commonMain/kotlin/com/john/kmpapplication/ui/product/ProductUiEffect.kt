package com.john.kmpapplication.ui.product

sealed interface ProductUiEffect {
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null
    ) : ProductUiEffect

    data object NavigateBack : ProductUiEffect
    data class NavigateToDetail(val id: Int?) : ProductUiEffect
}