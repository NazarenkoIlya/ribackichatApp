package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.model.MessageDraft
import com.example.rybackiapp.domain.repository.DataBaseMessageRepository
import javax.inject.Inject

class GetMessageDraftUseCase @Inject constructor(
    private val repository: DataBaseMessageRepository
) {
    suspend operator fun invoke(chatId: String): MessageDraft? {
        return repository.getMessage(chatId)
    }
}