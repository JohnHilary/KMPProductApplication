package com.john.kmpapplication.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.john.kmpapplication.ui.BaseScreen
import com.john.kmpapplication.ui.component.AppImage
import com.john.kmpapplication.ui.component.FullScreenLoader
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProductDetailsScreen(
    navController: NavController = rememberNavController(),
    uiState: ProductDetailUiState = ProductDetailUiState.UnInitialized,
    uiEffect: Flow<ProductDetailUiEffect>? = null,
    onEvent: (ProductDetailUiEvent) -> Unit = {},
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


    LaunchedEffect(Unit) {
        uiEffect?.collect { effect ->
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
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(
                top = (it.calculateTopPadding() + 16.dp),
                start = 16.dp,
                end = 16.dp,
                bottom = (it.calculateBottomPadding() + 16.dp)
            )
        ) {
        when (uiState) {
            ProductDetailUiState.Loading -> ProductDetailShimmer()

            ProductDetailUiState.NoData -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No Data available")
                }
            }

            is ProductDetailUiState.ShowData -> {

                    Spacer(modifier = Modifier.height(12.dp))
                    AppImage(
                        imageUrl = uiState.product.image,
                        modifier = Modifier.fillMaxWidth().background(Color.LightGray)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = uiState.product.title, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = uiState.product.price.toString(), fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = uiState.product.description)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = uiState.product.category)

                FullScreenLoader(isLoading = uiState.isLoading)
            }

            ProductDetailUiState.UnInitialized -> Unit
        }
}
    }
}

@Composable
fun ProductDetailShimmer() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .shimmer()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Color.LightGray,
                    RoundedCornerShape(12.dp)
                )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(24.dp)
                .background(
                    Color.LightGray,
                    RoundedCornerShape(8.dp)
                )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(20.dp)
                .background(
                    Color.LightGray,
                    RoundedCornerShape(8.dp)
                )
        )

        Spacer(modifier = Modifier.height(16.dp))

        repeat(4) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
                    .padding(vertical = 4.dp)
                    .background(
                        Color.LightGray,
                        RoundedCornerShape(8.dp)
                    )
            )
        }
    }
}