package com.example.shoes.net

import retrofit2.http.*

// DTOs
data class TokenResponse(val token: String)
data class LoginRequest(val username: String, val password: String)
data class RegisterRequest(val username: String, val password: String)

data class ProductDto(
    val id: Long,
    val name: String,
    val subtitle: String?,
    val price: Int,
    val stock: Int?
)

data class CartItemDto(
    val id: Long,
    val product: ProductDto,
    val quantity: Int
)

data class AddCartItemRequest(val productId: Long, val quantity: Int)
data class UpdateCartItemRequest(val quantity: Int)

data class OrderItemDto(
    val id: Long,
    val productName: String,
    val price: Int,
    val quantity: Int
)
data class OrderDto(
    val id: Long,
    val totalAmount: Int,
    val status: String,
    val items: List<OrderItemDto>
)

interface AuthApi {
    @POST("api/auth/register")
    suspend fun register(@Body req: RegisterRequest): TokenResponse

    @POST("api/auth/login")
    suspend fun login(@Body req: LoginRequest): TokenResponse
}

interface ProductApi {
    @GET("api/products")
    suspend fun list(): List<ProductDto>
}

interface CartApi {
    @GET("api/cart")
    suspend fun list(): List<CartItemDto>

    @POST("api/cart/items")
    suspend fun add(@Body req: AddCartItemRequest): CartItemDto

    @PATCH("api/cart/items/{id}")
    suspend fun update(@Path("id") id: Long, @Body req: UpdateCartItemRequest): CartItemDto

    @DELETE("api/cart/items/{id}")
    suspend fun delete(@Path("id") id: Long)
}

interface OrderApi {
    @POST("api/orders")
    suspend fun create(): OrderDto

    @GET("api/orders")
    suspend fun list(): List<OrderDto>

    @GET("api/orders/{id}")
    suspend fun get(@Path("id") id: Long): OrderDto

    @POST("api/orders/{id}/pay")
    suspend fun pay(@Path("id") id: Long): OrderDto
}
