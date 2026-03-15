package com.john.kmpapplication.domain

import com.john.kmpapplication.data.Product
import com.john.kmpapplication.data.remote.ApiResult
import com.john.kmpapplication.data.remote.handleApi

class ProductRepository(
    private val productService: ProductService
) {
    suspend fun getProducts(): ApiResult<List<Product>> {
        return handleApi {
            productService.getProducts()
        }
    }

    suspend fun getCategories(): ApiResult<List<String>> {
        return handleApi {
            productService.getCategories()
        }
    }

    suspend fun getProduct(id: Int?): ApiResult<Product> {
        return handleApi {
            productService.getProduct(id)
        }
    }

}