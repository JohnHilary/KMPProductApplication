package com.john.kmpapplication.ui.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.john.kmpapplication.data.Product
import com.john.kmpapplication.data.remote.ApiResult
import com.john.kmpapplication.domain.ProductRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val repository: ProductRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<ProductDetailScreen>()

    val id = route.productId
    private val _uiState: MutableStateFlow<ProductDetailUiState> = MutableStateFlow(ProductDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<ProductDetailUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        getProduct(id = id)
    }

    fun onEvent(productDetailUiEvent: ProductDetailUiEvent) {
        when (productDetailUiEvent) {
            is ProductDetailUiEvent.GetProductDetail -> getProduct(id = id)
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
                is ApiResult.Success -> _uiState.update {
                    it.copy(
                        product = result.data,
                        isLoading = false,
                        noData = result.data as? Product? == null
                    )
                }

                is ApiResult.Error -> {
                    _uiEffect.send(ProductDetailUiEffect.ShowSnackbar(message = result.message, actionLabel = "Retry"))
                    _uiState.update { it.copy(noData = true) }
                    setLoading(false)
                }

                is ApiResult.Exception -> {
                    _uiEffect.send(
                        ProductDetailUiEffect.ShowSnackbar(
                            message = result.throwable.message ?: "Something went wrong", actionLabel = "Retry"
                        )
                    )
                    _uiState.update { it.copy(noData = true) }
                    setLoading(false)
                }
            }
        }
    }

}