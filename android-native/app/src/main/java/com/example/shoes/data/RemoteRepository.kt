package com.example.shoes.data

import com.example.shoes.model.Product
import com.example.shoes.net.*

class RemoteRepository {
    private val retrofit by lazy { ApiClient.get { Session.token } }
    private val authApi by lazy { retrofit.create(AuthApi::class.java) }
    private val productApi by lazy { retrofit.create(ProductApi::class.java) }

    suspend fun login(username: String, password: String): String {
        val token = authApi.login(LoginRequest(username, password)).token
        Session.token = token
        return token
    }

    suspend fun register(username: String, password: String): String {
        val token = authApi.register(RegisterRequest(username, password)).token
        Session.token = token
        return token
    }

    suspend fun products(): List<Product> = productApi.list().map {
        Product(
            id = it.id.toString(),
            name = it.name,
            price = it.price / 100.0,
            stock = it.stock ?: 0,
            primaryImageUrl = null
        )
    }
}
