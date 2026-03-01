package com.john.kmpapplication.ui.product

sealed interface ProductDetailUiEvent {
    data class GetProductDetail(val id: Int?) : ProductDetailUiEvent
}