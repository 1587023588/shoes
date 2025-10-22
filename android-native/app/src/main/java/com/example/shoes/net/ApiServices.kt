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

// Chat DTOs & APIs
data class ChatConversationDto(
    val id: Long,
    val type: String?,
    val name: String?,
)

data class EnsureDmRequest(val userId: Long)
data class CreateGroupRequest(val name: String, val memberIds: List<Long>)
data class EnsureDmResponse(val conversationId: Long)
data class CreateGroupResponse(val conversationId: Long)

data class ChatMessageDto(
    val id: Long?,
    val conversationId: Long?,
    val senderId: Long?,
    val content: String?,
    val createdAt: String?
)

interface ChatApi {
    @GET("api/chat/conversations")
    suspend fun listConversations(): List<ChatConversationDto>

    @POST("api/chat/conversations/dm")
    suspend fun ensureDm(@Body req: EnsureDmRequest): EnsureDmResponse

    @POST("api/chat/conversations/group")
    suspend fun createGroup(@Body req: CreateGroupRequest): CreateGroupResponse

    @GET("api/chat/conversations/{id}/messages")
    suspend fun getMessages(
        @Path("id") id: Long,
        @Query("size") size: Int? = 20,
        @Query("before") before: String? = null
    ): List<ChatMessageDto>

    @DELETE("api/chat/conversations/{id}")
    suspend fun deleteConversation(@Path("id") id: Long, @Query("force") force: Boolean? = null)
}
