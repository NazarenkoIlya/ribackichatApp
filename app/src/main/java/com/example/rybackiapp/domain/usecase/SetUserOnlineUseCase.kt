package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.UserStatusRepository
import javax.inject.Inject

class SetUserOnlineUseCase @Inject constructor(
    private val userStatusRepository: UserStatusRepository
) {
    suspend operator fun invoke(userId: String) {
        userStatusRepository.setUserOnline(userId)
    }
}