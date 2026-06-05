package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.NotificationRepository
import javax.inject.Inject

class SaveFcmTokenUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(uid: String, token: String) {
        repository.saveFcmToken(uid, token)
    }
}