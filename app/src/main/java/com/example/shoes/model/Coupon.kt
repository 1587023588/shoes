package com.example.shoes.model

data class Coupon(
    val id: String,
    val amount: Int,           // 立减金额
    val threshold: Int,        // 满多少可用
    val title: String,
    val desc: String,
    var claimed: Boolean = false
)
