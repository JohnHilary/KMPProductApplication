package com.john.kmpapplication.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.kmpapplication.data.Product
import com.john.kmpapplication.domain.ProductRepository
import com.john.kmpapplication.data.remote.ApiResult
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
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
        initData()
    }

    fun initData() {
        viewModelScope.launch {
            setLoading(isLoading = true)
            val (productsResult, categoriesResult) = loadData()
            handleSuccess(productsResult, categoriesResult)
            handleError(productsResult, categoriesResult)
            setLoading(isLoading = false)
        }
    }

    private suspend fun loadData(): Pair<ApiResult<List<Product>>, ApiResult<List<String>>> =
        coroutineScope {
            val productsDeferred = async { getProducts() }
            val categoriesDeferred = async { getCategories() }
            productsDeferred.await() to categoriesDeferred.await()
        }

    private suspend fun handleError(
        productsResult: ApiResult<*>,
        categoriesResult: ApiResult<*>
    ) {
        val error = listOf(productsResult, categoriesResult)
            .firstOrNull { it is ApiResult.Error || it is ApiResult.Exception }
        error?.let {
            val message = when (it) {
                is ApiResult.Error -> it.message
                is ApiResult.Exception -> it.throwable.message ?: "Something went wrong"
                else -> ""
            }
            _uiEffect.send(ProductUiEffect.ShowSnackbar(message))
        }
    }

    private fun handleSuccess(
        productsResult: ApiResult<List<Product>>,
        categoriesResult: ApiResult<List<String>>
    ) {
        val categories = listOf("All") + ((categoriesResult as? ApiResult.Success)?.data ?: emptyList())
        _uiState.update { state ->
            state.copy(
                allProducts = (productsResult as? ApiResult.Success)?.data ?: state.allProducts,
                products = (productsResult as? ApiResult.Success)?.data ?: state.products,
                categories = categories
            )
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    fun onEvent(productUiEvent: ProductUiEvent) {
        when (productUiEvent) {
            is ProductUiEvent.OnFilterItemClicked -> {
                viewModelScope.launch {
                    setLoading(isLoading = true)
                    delay(1000)
                    onCategorySelected(category = productUiEvent.item)
                    setLoading(isLoading = false)
                }
            }
        }
    }

    private fun onCategorySelected(category: String) {
        _uiState.update {
            it.copy(selectedCategory = category)
        }
    }

    private suspend fun getCategories() = repository.getCategories()

    private suspend fun getProducts() = repository.getProducts()


}