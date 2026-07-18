package com.example.model

data class Product(
    val productId: Int,
    val category: String,
    val name: String,
    val description: String,
    val material: String,
    val size: String,
    val price: Double,
    val image: String
)
