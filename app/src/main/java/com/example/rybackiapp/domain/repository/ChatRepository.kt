package com.example.rybackiapp.domain.repository

import com.example.rybackiapp.domain.model.Chat
import com.example.rybackiapp.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun createPrivateChat(
        chatId: String,
        participants: List<String>,
        lastMessage: String,
        lastTimestamp: Long
    ): String

    suspend fun isChatCreated(chatId: String): Boolean
    suspend fun sendMessage(chatId: String, message: Message)
    suspend fun deleteMessage(chatId: String, messageId: String)
    suspend fun deletePrivateChat(chatId: String)
    suspend fun editMessage(chatId: String, messageId: String, text: String)
    fun observeMessages(chatId: String): Flow<List<Message>>
    fun observeUserChats(uid: String): Flow<List<Chat>>
    fun observeUnreadChatsCount(uid: String): Flow<Int>
    fun observeUnreadMessages(uid: String, chatId: String): Flow<Int>
    suspend fun resetUnreadCount(uid: String, chatId: String)
}