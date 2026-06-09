package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.UserStatusRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserOnlineUseCase @Inject constructor(
    private val userStatusRepository: UserStatusRepository
) {
    operator fun invoke(userId: String): Flow<Boolean> {
        return userStatusRepository.observeUserOnline(userId)
    }
}