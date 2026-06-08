package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.SettingsRepository
import javax.inject.Inject

class SetFontSizeMassageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(size: Int): Result<Unit> {
        return try {
            settingsRepository.setFontSizeMessage(size)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}