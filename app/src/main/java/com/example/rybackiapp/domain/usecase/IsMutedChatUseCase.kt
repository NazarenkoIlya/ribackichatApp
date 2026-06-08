package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsMutedChatUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(chatId: String): Flow<Boolean> {
        return settingsRepository.isChatMuted(chatId)
    }
}