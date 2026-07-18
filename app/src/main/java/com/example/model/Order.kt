package com.example.model

data class Order(
    val orderId: Int,
    val userId: Int,
    val productId: Int,
    val quantity: Int,
    val paperType: String,
    val customText: String?,
    val designPath: String?,
    val deliveryMethod: String,
    val status: String,
    val date: String,
    val totalPrice: Double,
    var productName: String = "",
    var productImage: String = ""
)
