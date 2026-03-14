package com.john.kmpapplication.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.kmpapplication.data.Product
import com.john.kmpapplication.domain.ProductRepository
import com.john.kmpapplication.data.remote.ApiResult
import com.john.kmpapplication.ui.product.ProductUiEffect.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOn
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
        initData()
    }

    private fun initData() {
        viewModelScope.launch {
            try {
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

                _uiState.update {
                    it.copy(
                        allProducts = products,
                        products = products,
                        categories = categories,
                    )
                }
                setLoading(false)
                observeFilters()

            } catch (e: Exception) {
                setLoading(false)
                _uiEffect.send(
                    ShowSnackbar(
                        e.message ?: "Something went wrong"
                    )
                )
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeFilters() {
        combine(
            uiState.map { it.selectedCategory }.distinctUntilChanged(),
            uiState.map { it.searchQuery }.debounce(300).distinctUntilChanged(),
            uiState.map { it.allProducts }.distinctUntilChanged()
        )
        { category, query, products ->

            products.filter { product ->

                val categoryMatch =
                    category == null || category == "All" || product.category == category

                val searchMatch =
                    query.isBlank() || product.title.contains(query, true)

                categoryMatch && searchMatch
            }
        }.flowOn(Dispatchers.Default).drop(1).onEach {
            setLoading(true)
        }.map { filteredProducts ->
            delay(300)
            filteredProducts
        }.onEach { filteredProducts ->
            _uiState.update {
                it.copy(products = filteredProducts)
            }
            setLoading(false)
        }.launchIn(viewModelScope)
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
                    _uiEffect.send(NavigateToDetail(productUiEvent.id))
                }
            }

            is ProductUiEvent.OnSearchQueryChanged -> onSearchQuery(query = productUiEvent.query)
        }
    }

    private fun onCategorySelected(category: String) {
        _uiState.update {
            it.copy(selectedCategory = category)
        }
    }

    private fun onSearchQuery(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }

    private suspend fun getCategories() = repository.getCategories()

    private suspend fun getProducts() = repository.getProducts()


}