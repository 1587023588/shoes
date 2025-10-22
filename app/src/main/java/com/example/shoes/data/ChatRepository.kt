package com.example.shoes.data

import com.example.shoes.net.*

class ChatRepository {
    private val retrofit by lazy { ApiClient.get { Session.token } }
    private val api by lazy { retrofit.create(ChatApi::class.java) }

    suspend fun listConversations() = api.listConversations()
    suspend fun ensureDm(userId: Long) = api.ensureDm(EnsureDmRequest(userId))
    suspend fun createGroup(name: String, memberIds: List<Long>) = api.createGroup(CreateGroupRequest(name, memberIds))
    suspend fun getMessages(conversationId: Long, size: Int = 20, before: String? = null) = api.getMessages(conversationId, size, before)
}
