package com.john.kmpapplication.ui.product

import com.john.kmpapplication.data.Product

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val product: Product? = null
)
