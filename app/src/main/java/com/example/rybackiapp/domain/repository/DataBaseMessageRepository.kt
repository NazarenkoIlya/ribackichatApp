package com.example.rybackiapp.domain.repository

import com.example.rybackiapp.domain.model.MessageDraft

interface DataBaseMessageRepository {
    suspend fun insertMessage(mes: MessageDraft)
    suspend fun getMessage(chatId: String): MessageDraft?
    suspend fun deleteMessage(chatId: String)
}