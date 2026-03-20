package com.john.kmpapplication.di

import com.john.kmpapplication.data.LoginResponse
import com.john.kmpapplication.data.TokenManager
import com.john.kmpapplication.db.AppDatabase
import com.john.kmpapplication.db.createDatabase
import com.john.kmpapplication.util.Constant
import com.john.kmpapplication.domain.ProductService
import com.john.kmpapplication.domain.ProductRepository
import com.john.kmpapplication.domain.UserRepository
import com.john.kmpapplication.domain.UserService
import com.john.kmpapplication.ui.component.signup.SignUpViewModel
import com.john.kmpapplication.ui.login.LoginViewModel
import com.john.kmpapplication.ui.product.ProductDetailViewModel
import com.john.kmpapplication.ui.product.ProductViewModel
import com.john.kmpapplication.ui.profile.ProfileViewModel
import com.john.kmpapplication.util.CLIENT_2
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
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
    single(named(CLIENT_2)) {
        val tokenManager = get<TokenManager>()

        HttpClient {
            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = tokenManager.getAccessToken()
                        val refreshToken = tokenManager.getRefreshToken()
                        if (accessToken != null && refreshToken != null) {
                            BearerTokens(
                                accessToken = accessToken,
                                refreshToken = refreshToken
                            )
                        } else null
                    }
                    refreshTokens {
                        if (oldTokens?.refreshToken.isNullOrEmpty()) {
                            return@refreshTokens null
                        }

                        val response = client.post("/api/v1/auth/refresh-token") {
                            markAsRefreshTokenRequest()
                            setBody(mapOf("refreshToken" to oldTokens?.refreshToken))
                            contentType(ContentType.Application.Json)
                        }

                        if (response.status == HttpStatusCode.Created || response.status == HttpStatusCode.OK) {
                            val newTokens: LoginResponse = response.body()

                            tokenManager.saveAccessToken(newTokens.accessToken)
                            tokenManager.saveRefreshToken(newTokens.refreshToken)

                            BearerTokens(
                                accessToken = newTokens.accessToken,
                                refreshToken = newTokens.refreshToken
                            )
                        } else {
                            tokenManager.triggerLogout()
                            null
                        }
                    }
                    sendWithoutRequest { request ->
                        val path = request.url.encodedPath
                        !path.contains("/auth/login") || !path.contains("/auth/refresh-token")
                    }
                }
            }
            defaultRequest {
                url(Constant.BASEURL2)
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
    viewModel { ProductDetailViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    factory { UserService(get(named(CLIENT_2))) }

    single {
        createDatabase(get())
    }
    single {
        TokenManager(get())
    }
    factory { get<AppDatabase>().userDao() }
    viewModel { ProfileViewModel(get()) }
    factory { UserRepository(get(), get(),get()) }
    viewModel { SignUpViewModel(get()) }

}
