package com.example.shoes.order

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class OrderItem(
    val productId: String,
    val title: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String?
)

data class Order(
    val orderNo: String,
    val createTime: Long,
    val totalAmount: Double,
    val totalQuantity: Int,
    val status: String = "paid",
    val items: List<OrderItem>
)

class LocalOrderStore private constructor(ctx: Context) {
    private val prefs: SharedPreferences =
        ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    private fun loadAll(): MutableList<Order> {
        val raw = prefs.getString(KEY_ORDERS, null) ?: return mutableListOf()
        return try {
            val type = object : TypeToken<List<Order>>() {}.type
            gson.fromJson<List<Order>>(raw, type)?.toMutableList() ?: mutableListOf()
        } catch (_: Throwable) {
            mutableListOf()
        }
    }

    private fun saveAll(list: List<Order>) {
        prefs.edit().putString(KEY_ORDERS, gson.toJson(list)).apply()
    }

    fun create(items: List<OrderItem>): Order {
        require(items.isNotEmpty()) { "empty items" }
        var totalAmount = 0.0
        var totalQty = 0
        items.forEach {
            totalQty += it.quantity
            totalAmount += it.price * it.quantity
        }
        val order = Order(
            orderNo = System.currentTimeMillis().toString() + (1000..9999).random(),
            createTime = System.currentTimeMillis(),
            totalAmount = totalAmount,
            totalQuantity = totalQty,
            items = items
        )
        val all = loadAll()
        all.add(0, order)
        saveAll(all)
        return order
    }

    fun list(): List<Order> = loadAll()

    fun get(orderNo: String): Order? = loadAll().find { it.orderNo == orderNo }

    companion object {
        private const val PREF_NAME = "orders.store"
        private const val KEY_ORDERS = "orders"

        @Volatile private var instance: LocalOrderStore? = null

        fun get(ctx: Context): LocalOrderStore = instance ?: synchronized(this) {
            instance ?: LocalOrderStore(ctx.applicationContext).also { instance = it }
        }
    }
}
