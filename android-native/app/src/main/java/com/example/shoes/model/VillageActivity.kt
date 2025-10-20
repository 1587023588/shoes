package com.example.shoes.model

data class VillageActivity(
    val id: String,
    val title: String,
    val time: String,
    val location: String,
    val brief: String,
    val status: String // 报名中/已结束/进行中 等
)
