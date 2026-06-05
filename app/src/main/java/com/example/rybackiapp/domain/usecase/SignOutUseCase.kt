package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.AuthRepository
import com.example.rybackiapp.domain.repository.NotificationRepository
import com.example.rybackiapp.domain.repository.UserIdCacheRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userIdCacheRepository: UserIdCacheRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        userIdCacheRepository.onUserLogout()
        return authRepository.signOut()
    }
}