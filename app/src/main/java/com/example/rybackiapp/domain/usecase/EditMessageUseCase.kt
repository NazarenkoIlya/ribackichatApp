package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.ChatRepository
import javax.inject.Inject

class EditMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(chatId: String, messageId: String, text: String): Result<Unit> {
        return try {
            chatRepository.editMessage(
                chatId = chatId,
                messageId = messageId,
                text = text
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}