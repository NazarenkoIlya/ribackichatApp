package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.DataBaseMessageRepository
import javax.inject.Inject

class DeleteMessageDraftUseCase @Inject constructor(
    private val repository: DataBaseMessageRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteMessage(id)
    }
}