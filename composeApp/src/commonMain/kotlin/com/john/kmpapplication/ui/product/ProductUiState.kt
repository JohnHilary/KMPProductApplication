package com.john.kmpapplication.ui.product

import com.john.kmpapplication.data.Product

data class ProductUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null
)