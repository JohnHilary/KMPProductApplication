package com.john.kmpapplication.ui.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.john.kmpapplication.data.Product
import com.john.kmpapplication.ui.component.FullScreenLoader
import com.john.kmpapplication.ui.component.ProductImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Preview
@Composable
fun ProductScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    uiState: ProductUiState = ProductUiState(),
    uiEffect: Flow<ProductUiEffect>? = null,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onEvent: (ProductUiEvent) -> Unit = {}
) {

    val scope = rememberCoroutineScope()

    LaunchedEffect(uiEffect) {
        uiEffect?.collect {
            when (it) {
                ProductUiEffect.NavigateBack -> {
                    navController.navigateUp()
                }

                is ProductUiEffect.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(message = it.message)
                    }
                }
            }

        }
    }

    if (uiState.products.isNotEmpty()) {
        LazyColumn(
            modifier = modifier, contentPadding = PaddingValues(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.products, key = {
                it.id
            }) {
                ProductItem(
                    modifier = Modifier.heightIn(min = 200.dp),
                    product = it,
                    onClick = {

                    })
            }
        }
    } else if (!uiState.isLoading && uiState.products.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "No products available", modifier = Modifier.align(Alignment.Center))
        }
    }


    FullScreenLoader(isLoading = uiState.isLoading)

}


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
            Text(text = product.description)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.category)
        }
    }

}
