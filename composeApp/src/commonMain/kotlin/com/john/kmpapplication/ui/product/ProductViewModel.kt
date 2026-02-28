package com.john.kmpapplication.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.kmpapplication.domain.ProductRepository
import com.john.kmpapplication.data.remote.ApiResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ProductUiState> = MutableStateFlow(ProductUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<ProductUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        getProducts()
    }


    fun onEvent(productUiEvent: ProductUiEvent) {

    }

    fun getProducts() {
        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true) }

            when (val result = repository.getProducts()) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            products = result.data,
                            isLoading = false)
                    }
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(
                        ProductUiEffect.ShowSnackbar(result.message)
                    )

                }
                is ApiResult.Exception -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(
                        ProductUiEffect.ShowSnackbar(
                            result.throwable.message ?: "Unexpected error"
                        )
                    )
                }
            }
        }
    }


}