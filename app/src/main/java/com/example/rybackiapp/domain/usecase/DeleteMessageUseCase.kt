package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.ChatRepository
import javax.inject.Inject

class DeleteMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(chatId: String, messageId: String): Result<Unit> {
        return try {
            chatRepository.deleteMessage(
                chatId = chatId,
                messageId = messageId
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}