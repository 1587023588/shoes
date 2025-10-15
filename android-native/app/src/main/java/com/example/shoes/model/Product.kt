package com.example.shoes.model

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val stock: Int,
    val craft: String? = null,
    val material: String? = null,
    val sizeRange: String? = null,
    val thumbnail: String? = null
)
