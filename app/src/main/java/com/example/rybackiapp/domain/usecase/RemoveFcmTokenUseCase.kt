package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.NotificationRepository
import javax.inject.Inject

class RemoveFcmTokenUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(uid: String) {
        repository.removeFcmToken(uid)
    }
}