package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.model.Chat
import com.example.rybackiapp.domain.repository.ChatRepository
import com.example.rybackiapp.domain.repository.UserIdCacheRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveUnreadMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(chatId: String, uid: String): Flow<Int> =
        repository.observeUnreadMessages(uid, chatId)

}