package com.john.kmpapplication.ui.product

import com.john.kmpapplication.data.Product

sealed interface ProductUiState {
    data object Loading : ProductUiState
    data class ShowData(
        val isRefreshing : Boolean = false,
        val allProducts: List<Product> = emptyList(),
        val products: List<Product> = emptyList(),
        val categories: List<String> = emptyList(),
        val selectedCategory: String? = null,
        val searchQuery: String = ""
    ) : ProductUiState
    data object NoData : ProductUiState
}
