package com.john.kmpapplication.presentation.event

sealed interface ProductUiEffect {
    data class ShowSnackbar(val message: String) : ProductUiEffect
    data object NavigateBack : ProductUiEffect
}