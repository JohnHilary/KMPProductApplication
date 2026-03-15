package com.john.kmpapplication.di

import com.john.kmpapplication.db.AppDatabase
import com.john.kmpapplication.db.createDatabase
import com.john.kmpapplication.util.Constant
import com.john.kmpapplication.domain.ProductService
import com.john.kmpapplication.domain.ProductRepository
import com.john.kmpapplication.domain.UserRepository
import com.john.kmpapplication.domain.UserService
import com.john.kmpapplication.ui.login.LoginViewModel
import com.john.kmpapplication.ui.product.ProductDetailViewModel
import com.john.kmpapplication.ui.product.ProductViewModel
import com.john.kmpapplication.ui.profile.ProfileViewModel
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
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

            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println("KtorNetwork: $message")
                    }
                }

            }
        }
    }
    factory { ProductService(get()) }
    factory { ProductRepository(get()) }
    viewModel { ProductViewModel(get()) }
    viewModel { ProductDetailViewModel(get(),get()) }
    viewModel { LoginViewModel(get()) }
    factory { UserService(get()) }

    single {
        createDatabase(get())
    }
    factory { get<AppDatabase>().userDao() }
    viewModel { ProfileViewModel(get()) }
    factory { UserRepository(get(),get()) }

}
