package com.john.kmpapplication.presentation.uistate

import com.john.kmpapplication.data.Product

data class ProductUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList()
)