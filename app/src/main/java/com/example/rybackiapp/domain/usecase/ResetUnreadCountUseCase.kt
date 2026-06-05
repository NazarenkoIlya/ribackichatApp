package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.ChatRepository
import com.example.rybackiapp.domain.repository.ProfileRepository
import com.example.rybackiapp.domain.repository.UserIdCacheRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ResetUnreadCountUseCase @Inject constructor(
    private val repository: ChatRepository,
    private val userIdCacheRepository: UserIdCacheRepository
) {
    suspend operator fun invoke(chatId: String) {
        val uid = userIdCacheRepository.observeUserId().first()
        repository.resetUnreadCount(uid, chatId)
    }

}