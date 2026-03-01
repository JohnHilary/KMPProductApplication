package com.john.kmpapplication.di

import com.john.kmpapplication.util.Constant
import com.john.kmpapplication.domain.ApiService
import com.john.kmpapplication.domain.ProductRepository
import com.john.kmpapplication.ui.product.ProductDetailViewModel
import com.john.kmpapplication.ui.product.ProductViewModel
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        HttpClient {
            defaultRequest {
                url(Constant.BASEURL)
            }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
    single { ApiService(get()) }
    factory { ProductRepository(get()) }
    viewModel { ProductViewModel(get()) }
    viewModel { ProductDetailViewModel(get()) }
}
