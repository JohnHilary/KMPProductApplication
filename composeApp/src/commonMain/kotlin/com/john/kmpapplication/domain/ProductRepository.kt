package com.john.kmpapplication.domain

import com.john.kmpapplication.data.Product
import com.john.kmpapplication.data.remote.ApiResult
import com.john.kmpapplication.data.remote.handleApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepository(
    private val apiService: ApiService
) {
    suspend fun getProducts(): ApiResult<List<Product>> {
        return handleApi {
            apiService.getProducts()
        }
    }

    suspend fun getCategories(): ApiResult<List<String>> {
        return handleApi {
            apiService.getCategories()
        }
    }

    suspend fun getProduct(id: Int?): ApiResult<Product> {
        return handleApi {
            apiService.getProduct(id)
        }
    }

}