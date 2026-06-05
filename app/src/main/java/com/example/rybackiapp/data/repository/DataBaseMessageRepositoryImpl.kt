package com.example.rybackiapp.data.repository

import com.example.rybackiapp.data.mappers.toMapMessageDraft
import com.example.rybackiapp.data.mappers.toMapMessagesDbo
import com.example.rybackiapp.data.model.room.dao.MessageDao
import com.example.rybackiapp.domain.model.MessageDraft
import com.example.rybackiapp.domain.repository.DataBaseMessageRepository
import javax.inject.Inject

class DataBaseMessageRepositoryImpl @Inject constructor(
    val messageDao: MessageDao
) : DataBaseMessageRepository {
    override suspend fun insertMessage(mes: MessageDraft) {
        messageDao.insertMessage(mes.toMapMessagesDbo())
    }

    override suspend fun getMessage(chatId: String): MessageDraft? {
        return messageDao.getMessage(chatId)?.toMapMessageDraft()
    }

    override suspend fun deleteMessage(chatId: String) {
        messageDao.deleteMessageById(chatId)
    }
}