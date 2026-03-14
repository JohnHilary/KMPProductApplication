package com.john.kmpapplication.ui.product

sealed interface ProductDetailUiEvent {
    data object GetProductDetail : ProductDetailUiEvent
}