package com.example.model

data class Address(
    val addressId: Int,
    val userId: Int,
    val fullAddress: String,
    val city: String,
    val postalCode: String
)
