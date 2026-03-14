package com.john.kmpapplication.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.john.kmpapplication.data.Product
import com.john.kmpapplication.ui.component.FilterChips
import com.john.kmpapplication.ui.component.FullScreenLoader
import com.john.kmpapplication.ui.component.ProductImage
import com.john.kmpapplication.ui.component.SearchBar
import com.john.kmpapplication.ui.component.TopBarHostState
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProductScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    uiState: ProductUiState = ProductUiState(),
    uiEffect: Flow<ProductUiEffect>? = null,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    topBarHostState: TopBarHostState = TopBarHostState(),
    onEvent: (ProductUiEvent) -> Unit = {}
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val focusManager = LocalFocusManager.current
    val searchBarColor = MaterialTheme.colorScheme.secondaryContainer
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


    val myColors = TopAppBarDefaults.topAppBarColors(
        containerColor = searchBarColor,
        scrolledContainerColor = searchBarColor
    )

    LaunchedEffect(Unit) {
        topBarHostState.update(
            behavior = scrollBehavior,
            colors = myColors
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                color = searchBarColor,
            ) {
                SearchBar(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                        .background(MaterialTheme.colorScheme.background, CircleShape),
                    query = uiState.searchQuery,
                    onDismissRequest = {
                        onEvent(ProductUiEvent.OnSearchQueryChanged(""))
                        focusManager.clearFocus()
                    },
                    onQueryChange = { query ->
                        onEvent(ProductUiEvent.OnSearchQueryChanged(query))
                    })
            }
        }
    }

    LifecycleResumeEffect(Unit) {
        onPauseOrDispose {
        topBarHostState.reset()
        }
    }

    LaunchedEffect(uiEffect) {
        uiEffect?.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )?.collect { effect ->
            when (effect) {
                ProductUiEffect.NavigateBack -> navController.navigateUp()
                is ProductUiEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
                is ProductUiEffect.NavigateToDetail -> navController.navigate(ProductDetailScreen(effect.id))
            }
        }
    }

    when {

        (uiState.allProducts.isNotEmpty()) -> {
            LazyColumn(
                modifier = modifier, contentPadding = PaddingValues(
                    bottom = 16.dp
                ), verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = "Products",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Spacer(modifier = Modifier.height(12.dp))
                    FilterChips(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        selectedItem = uiState.selectedCategory ?: uiState.categories[0],
                        items = uiState.categories,
                        onItemSelected = {
                            onEvent(ProductUiEvent.OnFilterItemClicked(item = it))
                        })
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (!uiState.isLoading) {
                    items(uiState.products, key = { it.id }) {
                        ProductItem(
                            modifier = Modifier.heightIn(min = 200.dp).padding(horizontal = 16.dp),
                            product = it,
                            onClick = {
                                onEvent(ProductUiEvent.NavigateToDetail(id = it.id))
                            })
                    }
                }
                if (!uiState.isLoading && uiState.products.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No result found"
                            )
                        }
                    }
                }
            }
        }

        (!uiState.isLoading && uiState.allProducts.isEmpty()) -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "No products available", modifier = Modifier.align(Alignment.Center))
            }
        }

    }

    FullScreenLoader(isLoading = uiState.isLoading)

}

@Serializable
data object ProductScreen


@Composable
fun ProductItem(modifier: Modifier = Modifier, product: Product, onClick: () -> Unit = {}) {
    Card(modifier = modifier, onClick = { onClick() }) {
        Column(modifier = Modifier.padding(16.dp)) {
            ProductImage(
                imageUrl = product.image, modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.title, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.price.toString(), fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.description, maxLines = 3, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.category)
        }
    }

}
