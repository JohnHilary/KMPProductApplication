package com.john.kmpapplication.ui.product

sealed interface ProductUiEvent {
    data class OnFilterItemClicked(val item: String) : ProductUiEvent
    data class NavigateToDetail(val id: Int?) : ProductUiEvent
}