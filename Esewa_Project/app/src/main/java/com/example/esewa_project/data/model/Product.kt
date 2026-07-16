package com.example.esewa_project.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("_id")
    val id: Int,

    val title: String,
    val des: String,
    val oldPrice: Double,
    val price: Double,
    val brand: String,
    val image: String,
    val isNew: Boolean,
    val category: String
)