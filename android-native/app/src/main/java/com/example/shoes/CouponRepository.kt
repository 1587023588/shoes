package com.example.shoes

import com.example.shoes.model.Coupon

object CouponRepository {
    fun list(): MutableList<Coupon> = mutableListOf(
        Coupon(id = "c1", amount = 50, threshold = 199, title = "新人专享神券", desc = "限指定款布鞋使用，叠加店铺满减"),
        Coupon(id = "c2", amount = 30, threshold = 159, title = "节日限时券", desc = "全店通用，部分联名款除外"),
        Coupon(id = "c3", amount = 80, threshold = 299, title = "大额神券", desc = "会员专享，限量发放")
    )
}
