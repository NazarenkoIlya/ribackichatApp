package com.example.rybackiapp.domain.usecase

import android.util.Log
import com.example.rybackiapp.domain.model.Message
import com.example.rybackiapp.domain.repository.ChatRepository
import com.example.rybackiapp.domain.repository.ProfileRepository
import com.example.rybackiapp.domain.repository.UserIdCacheRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SendMessageInPrivateChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userIdCacheRepository: UserIdCacheRepository,
    private val pushNotificationUseCase: PushNotificationUseCase
) {
    suspend operator fun invoke(
        chatId: String,
        text: String
    ): Result<Unit> {
        Log.d("NNNNN", "invoke: $text")

        return try {
            val senderId = userIdCacheRepository.observeUserId().first()
            val timestamp = System.currentTimeMillis()

            if (!chatRepository.isChatCreated(chatId = chatId)) {
                chatRepository.createPrivateChat(
                    chatId = chatId,
                    participants = chatId.split("_"),
                    lastMessage = "",
                    lastTimestamp = 0
                )
            }

            val message = Message(
                text = text,
                senderId = senderId,
                timestamp = timestamp
            )

            chatRepository.sendMessage(chatId, message)


            Log.d("YYYYYYYYY", "invoke: ${chatId.split("_").find { it != senderId } ?: ""}")
            pushNotificationUseCase.invoke(
                uid = chatId.split("_").find { it != senderId } ?: "",
                title = senderId,
                text = text,
                chatId = chatId
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}