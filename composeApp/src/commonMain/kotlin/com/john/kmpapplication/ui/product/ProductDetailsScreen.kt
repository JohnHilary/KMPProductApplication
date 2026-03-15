package com.john.kmpapplication.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.john.kmpapplication.ui.BaseScreen
import com.john.kmpapplication.ui.component.FullScreenLoader
import com.john.kmpapplication.ui.component.ProductImage
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailScreen(val productId: Int?)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProductDetailsScreen(
    navController: NavController = rememberNavController(),
    uiState: ProductDetailUiState = ProductDetailUiState(),
    uiEffect: Flow<ProductDetailUiEffect>? = null,
    onEvent: (ProductDetailUiEvent) -> Unit = {},
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


    LaunchedEffect(uiEffect) {
        uiEffect?.flowWithLifecycle(
            lifecycleOwner.lifecycle, Lifecycle.State.STARTED
        )?.collect { effect ->
            when (effect) {
                ProductDetailUiEffect.NavigateBack -> navController.navigateUp()
                is ProductDetailUiEffect.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = effect.message,
                        actionLabel = effect.actionLabel
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        onEvent(ProductDetailUiEvent.GetProductDetail)
                    }
                }
            }
        }

    }

    BaseScreen(navigationIcon = {
        IconButton(onClick = {
            navController.navigateUp()
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Clear"
            )
        }
    }, snackbarHostState = snackbarHostState, scrollBehavior = scrollBehavior, title = {
        Text(text = "Product Details", fontWeight = FontWeight.Bold)
    }) {
        if (uiState.noData) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No Data available")

            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(
                    top = (it.calculateTopPadding() + 16.dp),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = (it.calculateBottomPadding() + 16.dp)
                )
            ) {
                uiState.product?.let { product ->
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

}