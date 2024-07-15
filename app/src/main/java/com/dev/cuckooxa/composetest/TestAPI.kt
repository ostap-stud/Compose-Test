package com.dev.cuckooxa.composetest

import com.dev.cuckooxa.composetest.model.Products
import retrofit2.http.GET

interface TestAPI {
    @GET("products")
    suspend fun getAllProducts(): Products
}