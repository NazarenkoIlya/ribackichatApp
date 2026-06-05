package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.model.Chat
import com.example.rybackiapp.domain.model.PrivateChat
import com.example.rybackiapp.domain.repository.ChatRepository
import com.example.rybackiapp.domain.repository.ProfileRepository
import com.example.rybackiapp.domain.repository.UserIdCacheRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveUserChatsUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(id: String): Flow<List<Chat>> = chatRepository.observeUserChats(id)
}

