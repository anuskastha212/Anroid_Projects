package com.example.esewa_project.data.api

import com.example.esewa_project.data.model.MostPopular
import com.example.esewa_project.data.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("products")
    suspend fun getAllProducts(): List<Product>

    @GET("products/categories")
    suspend fun getMostPopular(): Response<List<MostPopular>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product
}
