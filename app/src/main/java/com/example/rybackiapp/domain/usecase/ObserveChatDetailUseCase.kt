package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.ChatRepository
import javax.inject.Inject

class ObserveChatDetailUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId: String) =
        chatRepository.observeMessages(chatId)
}