package com.john.kmpapplication.domain

import com.john.kmpapplication.data.Product
import com.john.kmpapplication.data.remote.ApiResult
import com.john.kmpapplication.data.remote.handleApi

class ProductRepository(
    private val apiService: ApiService
) {

    suspend fun getProducts(): ApiResult<List<Product>> {
        return handleApi {
            apiService.getProducts()
        }
    }
}