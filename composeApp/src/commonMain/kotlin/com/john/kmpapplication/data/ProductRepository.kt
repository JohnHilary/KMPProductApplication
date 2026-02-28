package com.john.kmpapplication.data

import com.john.kmpapplication.util.ApiResult
import com.john.kmpapplication.util.handleApi

class ProductRepository(
    private val apiService: ApiService
) {

    suspend fun getProducts(): ApiResult<List<Product>> {
        return handleApi {
            apiService.getProducts()
        }
    }
}