package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.ChatRepository
import javax.inject.Inject

class DeleteChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(chatId: String): Result<Unit> {
        return try {
            chatRepository.deletePrivateChat(chatId = chatId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}