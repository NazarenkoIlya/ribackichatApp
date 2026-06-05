package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUnreadChatsUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(uid: String): Flow<Int> =
        repository.observeUnreadChatsCount(uid)
}