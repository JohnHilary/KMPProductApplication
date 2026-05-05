package com.john.kmpapplication.ui.product

import com.john.kmpapplication.data.Product

sealed interface ProductDetailUiState {
    data object Loading : ProductDetailUiState
    data class ShowData(val product: Product, val isLoading: Boolean = false) :
        ProductDetailUiState

    data object NoData : ProductDetailUiState
    data object UnInitialized : ProductDetailUiState
}