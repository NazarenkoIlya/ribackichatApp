package com.example.rybackiapp.domain.usecase

import androidx.compose.runtime.traceEventEnd
import com.example.rybackiapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MuteChatUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(chatId: String): Result<Unit> {
        return try {
            settingsRepository.muteChat(chatId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}