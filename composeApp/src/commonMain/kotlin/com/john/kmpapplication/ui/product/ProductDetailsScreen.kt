package com.john.kmpapplication.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.john.kmpapplication.ui.component.FullScreenLoader
import com.john.kmpapplication.ui.component.ProductImage
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailScreen(val productId: Int?)

@Preview
@Composable
fun ProductDetailsScreen(
    navController: NavController = rememberNavController(),
    id: Int = 0,
    uiState: ProductDetailUiState = ProductDetailUiState(),
    uiEffect: Flow<ProductDetailUiEffect>? = null,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onEvent: (ProductDetailUiEvent) -> Unit = {},
) {

    val lifecycleOwner = LocalLifecycleOwner.current


    LaunchedEffect(id) {
        if (uiState.product?.id != id) {
            onEvent(ProductDetailUiEvent.GetProductDetail(id))
        }
    }

    LaunchedEffect(uiEffect) {
        uiEffect?.flowWithLifecycle(
            lifecycleOwner.lifecycle, Lifecycle.State.STARTED
        )?.collect { effect ->
            when (effect) {
                ProductDetailUiEffect.NavigateBack -> {
                    navController.navigateUp()
                }

                is ProductDetailUiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }

    }
    if (uiState.noData) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No Data available")

        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
        ) {
            uiState.product?.let { product ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Product Details", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                ProductImage(
                    imageUrl = product.image, modifier = Modifier.fillMaxWidth().background(Color.LightGray)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product.title, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product.price.toString(), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product.description)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product.category ?: "-")
            }
        }
    }
    FullScreenLoader(isLoading = uiState.isLoading)


}