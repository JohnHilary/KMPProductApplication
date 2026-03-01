package com.john.kmpapplication.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.kmpapplication.data.remote.ApiResult
import com.john.kmpapplication.domain.ProductRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val repository: ProductRepository
) : ViewModel() {


    private val _uiState: MutableStateFlow<ProductDetailUiState> = MutableStateFlow(ProductDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<ProductDetailUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(productDetailUiEvent: ProductDetailUiEvent) {
        when (productDetailUiEvent) {
            is ProductDetailUiEvent.GetProductDetail -> getProduct(id = productDetailUiEvent.id)
        }

    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    private fun getProduct(id: Int?) {
        viewModelScope.launch {
            setLoading(true)
            when (val result = repository.getProduct(id)) {
                is ApiResult.Success -> _uiState.update { it.copy(product = result.data, isLoading = false) }
                is ApiResult.Error -> {
                    _uiEffect.send(ProductDetailUiEffect.ShowSnackbar(message = result.message))
                    setLoading(false)
                }

                is ApiResult.Exception -> {
                    _uiEffect.send(
                        ProductDetailUiEffect.ShowSnackbar(
                            message = result.throwable.message ?: "Something went wrong"
                        )
                    )
                    setLoading(false)
                }
            }
        }
    }

}