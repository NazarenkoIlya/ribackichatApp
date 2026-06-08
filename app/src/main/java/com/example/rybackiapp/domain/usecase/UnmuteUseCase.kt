package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.SettingsRepository
import javax.inject.Inject

class UnmuteUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(chatId: String): Result<Unit> {
        return try {
            settingsRepository.unmuteChat(chatId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}