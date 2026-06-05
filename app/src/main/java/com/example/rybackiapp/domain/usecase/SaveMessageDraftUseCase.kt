package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.model.MessageDraft
import com.example.rybackiapp.domain.repository.DataBaseMessageRepository
import javax.inject.Inject

class SaveMessageDraftUseCase @Inject constructor(
    private val repository: DataBaseMessageRepository
) {
    suspend operator fun invoke(messageDraft: MessageDraft): Result<Unit> {
        return try {
            repository.insertMessage(messageDraft)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}