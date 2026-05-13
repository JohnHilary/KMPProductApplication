package com.john.kmpapplication.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.john.kmpapplication.data.Product
import com.john.kmpapplication.ui.BaseScreen
import com.john.kmpapplication.ui.component.AppImage
import com.john.kmpapplication.ui.component.FilterChips
import com.john.kmpapplication.ui.component.FullScreenLoader
import com.john.kmpapplication.ui.component.Screen
import com.john.kmpapplication.ui.component.SearchBar
import com.john.kmpapplication.ui.navigation.AnimatedBottomBar
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProductScreen(
    navController: NavController = rememberNavController(),
    uiState: ProductUiState = ProductUiState.UnInitialized,
    uiEffect: Flow<ProductUiEffect>? = null,
    onEvent: (ProductUiEvent) -> Unit = {}
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        uiEffect?.collect { effect ->
            when (effect) {
                ProductUiEffect.NavigateBack -> navController.navigateUp()
                is ProductUiEffect.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = effect.message,
                        actionLabel = effect.actionLabel
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        onEvent(ProductUiEvent.LoadData)
                    }
                }
                is ProductUiEffect.NavigateToDetail -> navController.navigate(Screen.ProductDetailScreen(effect.id))
            }
        }
    }

    BaseScreen(
        title = {
            SearchBar(
                modifier = Modifier.padding(end = 16.dp, top = 8.dp, bottom = 8.dp)
                    .background(MaterialTheme.colorScheme.background, CircleShape).fillMaxWidth(),
                query = (uiState as? ProductUiState.ShowData)
                    ?.searchQuery
                    .orEmpty(),
                onQueryChange = { query ->
                    onEvent(ProductUiEvent.OnSearchQueryChanged(query))
                })
        },
        snackbarHostState = snackbarHostState, scrollBehavior = scrollBehavior,
        bottomBar = {
            AnimatedBottomBar(
                navController = navController
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
            ) {
                Icon(Icons.Filled.Add, "Add")
            }
        }
    ) {
        when (uiState) {
            ProductUiState.Loading -> FullScreenLoader(isLoading = true)

            is ProductUiState.ShowData -> {
                LazyColumn(
                    modifier = Modifier.padding(
                        top = it.calculateTopPadding(),
                        bottom = (it.calculateBottomPadding() + 16.dp),
                        start = 16.dp,
                        end = 16.dp
                    ).fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Products",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        FilterChips(
                            modifier = Modifier.fillMaxWidth(),
                            selectedItem = uiState.selectedCategory ?: uiState.categories[0],
                            items = uiState.categories,
                            onItemSelected = {
                                onEvent(ProductUiEvent.OnFilterItemClicked(item = it))
                            })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    if (!uiState.isRefreshing) {
                        items(uiState.products, key = { it.id }) {
                            ProductItem(
                                modifier = Modifier.heightIn(min = 200.dp).fillMaxWidth(),
                                product = it,
                                onClick = {
                                    onEvent(ProductUiEvent.OnProductClick(id = it.id))
                                })
                        }
                    }
                    if (!uiState.isRefreshing && uiState.products.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No result found"
                                )
                            }
                        }
                    }
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            FullScreenLoader(isLoading = uiState.isRefreshing)
                        }
                    }

                }
            }

            ProductUiState.UnInitialized -> TODO()
            ProductUiState.NoData -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "No products available",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

    }
}




@Composable
fun ProductItem(modifier: Modifier = Modifier, product: Product, onClick: () -> Unit = {}) {
    Card(modifier = modifier, onClick = { onClick() }) {
        Column(modifier = Modifier.padding(16.dp)) {
            AppImage(
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
