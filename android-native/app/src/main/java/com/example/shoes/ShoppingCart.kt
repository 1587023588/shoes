package com.example.shoes

object ShoppingCart {
    private val map = mutableMapOf<String, Int>()

    fun add(id: String, count: Int = 1) {
        map[id] = (map[id] ?: 0) + count
    }

    fun count(id: String): Int = map[id] ?: 0

    fun all(): Map<String, Int> = map.toMap()
}
