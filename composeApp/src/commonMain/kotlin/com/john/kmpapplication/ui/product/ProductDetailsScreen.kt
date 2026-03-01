package com.john.kmpapplication.ui.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
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
    id : Int = 0,
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

     print("getProductDetail ${uiState}")
    LaunchedEffect(uiEffect) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect?.collect { effect ->
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
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp
        )
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Product Details", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            ProductImage(
                imageUrl = uiState.product?.image,
                modifier = Modifier.size(400.dp).clip(RoundedCornerShape(16.dp))
                    .align(Alignment.CenterHorizontally).padding(16.dp),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = uiState.product?.title ?: "-", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = uiState.product?.price.toString(), fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = uiState.product?.description ?: "-")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = uiState.product?.category ?: "-")
    }
    FullScreenLoader(isLoading = uiState.isLoading)


}