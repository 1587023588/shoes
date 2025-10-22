package com.example.shoes.model

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val oldPrice: Double? = null,
    val stock: Int,
    val craft: String? = null,
    val material: String? = null,
    val sizeRange: String? = null,
    val thumbnail: String? = null,
    val images: List<Int> = emptyList(),
    val intro: String? = null,
    val descImages: List<Int> = emptyList(),
    // 远程图片（优先使用，确保与小程序一致）
    val primaryImageUrl: String? = null,
    val imagesUrls: List<String> = emptyList(),
    val descImageUrls: List<String> = emptyList()
)
