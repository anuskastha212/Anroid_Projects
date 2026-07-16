package com.example.esewa_project.data.model

data class ProductResponse(
    val data: List<Product>,
    val totalProducts: Int,
    val totalPages: Int,
    val currentPage: Int,
    val perPage: Int
)