package com.john.kmpapplication.di

import com.john.kmpapplication.Constant
import com.john.kmpapplication.data.ApiService
import com.john.kmpapplication.data.ProductRepository
import com.john.kmpapplication.presentation.viewmodel.ProductViewModel
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val appModule = module {

    single {
        HttpClient {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = Constant.BASEURL
                }
            }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
    single { ApiService(get()) }
    single { ProductRepository(get()) }
    single<ProductViewModel> {
        ProductViewModel(get())
    }
}
