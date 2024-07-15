package com.dev.cuckooxa.composetest.model

data class Product (
    val id: Int,
    val title: String,
    val description: String,
    val price: Float,
    val images: List<String>
)