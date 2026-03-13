package com.john.kmpapplication.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.kmpapplication.data.Product
import com.john.kmpapplication.domain.ProductRepository
import com.john.kmpapplication.data.remote.ApiResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.filter

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ProductUiState> = MutableStateFlow(ProductUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<ProductUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        loadProducts()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun loadProducts() {
        flow {
            setLoading(true)
            val (productsResult, categoriesResult) = loadData()
            val products = when (productsResult) {
                is ApiResult.Success -> productsResult.data
                is ApiResult.Error -> throw Exception(productsResult.message)
                is ApiResult.Exception -> throw productsResult.throwable
            }
            val categories = when (categoriesResult) {
                is ApiResult.Success -> listOf("All") + categoriesResult.data
                is ApiResult.Error -> throw Exception(categoriesResult.message)
                is ApiResult.Exception -> throw categoriesResult.throwable
            }
            emit(products to categories)

        }.flatMapLatest { (allProducts, categories) ->
            _uiState.update {
                it.copy(
                    allProducts = allProducts,
                    categories = categories,
                )
            }
            uiState
                .map { it.selectedCategory }
                .debounce(300)
                .distinctUntilChanged()
                .onEach {
                    setLoading(true)
                    delay(300)
                }
                .map { category ->
                    allProducts.filter { product ->
                        category == null ||
                                category == "All" ||
                                product.category == category
                    }
                }
        }.onEach { filteredProducts ->
                _uiState.update {
                    it.copy(
                        products = filteredProducts,
                        isLoading = false
                    )
                }
            }
            .catch { e ->
                setLoading(false)
                _uiEffect.send(
                    ProductUiEffect.ShowSnackbar(
                        e.message ?: "Something went wrong"
                    )
                )
            }
            .launchIn(viewModelScope)
    }

    private suspend fun loadData(): Pair<ApiResult<List<Product>>, ApiResult<List<String>>> = coroutineScope {
        val productsDeferred = async { getProducts() }
        val categoriesDeferred = async { getCategories() }
        productsDeferred.await() to categoriesDeferred.await()
    }


    private fun setLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    fun onEvent(productUiEvent: ProductUiEvent) {
        when (productUiEvent) {
            is ProductUiEvent.OnFilterItemClicked -> onCategorySelected(category = productUiEvent.item)
            is ProductUiEvent.NavigateToDetail -> {
                viewModelScope.launch {
                    _uiEffect.send(ProductUiEffect.NavigateToDetail(productUiEvent.id))
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